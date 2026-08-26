package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
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
    @Final
    private ItemStackTemplate result;

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
            if (!foundInput && this.input.test(stack) && !stack.is(this.result.item())) {
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
        return List.of(
            new ShapelessCraftingRecipeDisplay(
                List.of(
                    this.input.display(),
                    this.material.display()
                ),
                new SlotDisplay.ItemStackSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemIds.CRAFTING_TABLE))
            )
        );
    }
}
