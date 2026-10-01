package net.errorcraft.itematic.mixin.world.item.crafting;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.references.BlockItemIds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.TransmuteRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(TransmuteRecipe.class)
public abstract class TransmuteRecipeExtender implements CraftingRecipe {
    @Shadow
    @Final
    private Ingredient input;

    @Shadow
    @Final
    private Ingredient material;

    @Shadow
    protected abstract int computeResultSize(int materialCount);

    @Shadow
    protected abstract SlotDisplay resultDisplay(int resultCount);

    @Shadow
    protected abstract int minMaterialCount();

    @Shadow
    protected abstract int maxMaterialCount();

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        boolean foundInput = false;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            final int index = i;
            if (!foundInput && this.input.test(stack)) {
                foundInput = true;
                this.input.itematic$remainder()
                    .map(ItemStackTemplate::create)
                    .ifPresent(remainder -> remainders.set(index, remainder));
                continue;
            }

            this.material.itematic$remainder()
                .map(ItemStackTemplate::create)
                .ifPresent(remainder -> remainders.set(index, remainder));
        }

        return remainders;
    }

    @Override
    public List<RecipeDisplay> itematic$display(HolderGetter<Item> items) {
        List<RecipeDisplay> displays = new ArrayList<>();
        List<SlotDisplay> ingredientSlots = new ArrayList<>();
        ingredientSlots.add(this.input.display());
        SlotDisplay materialDisplay = this.material.display();
        int minMaterialCount = this.minMaterialCount();
        int maxMaterialCount = this.maxMaterialCount();
        for (int materialCount = minMaterialCount; materialCount <= maxMaterialCount; ++materialCount) {
            ingredientSlots.add(materialDisplay);
            displays.add(
                new ShapelessCraftingRecipeDisplay(
                    List.copyOf(ingredientSlots),
                    this.resultDisplay(this.computeResultSize(materialCount)),
                    new SlotDisplay.ItemSlotDisplay(items.getOrThrow(BlockItemIds.CRAFTING_TABLE.item()))
                )
            );
        }

        return displays;
    }
}
