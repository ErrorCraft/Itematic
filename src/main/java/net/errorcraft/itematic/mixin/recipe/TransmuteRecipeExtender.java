package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(TransmuteRecipe.class)
public abstract class TransmuteRecipeExtender implements CraftingRecipe, RecipeAccess {
    @Shadow
    @Final
    Ingredient input;

    @Shadow
    @Final
    Ingredient material;

    @Shadow
    @Final
    TransmuteResult result;

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
                    .map(ItemStack::copy)
                    .ifPresent(remainder -> remainders.set(index, remainder));
            } else {
                this.material.itematic$remainder()
                    .map(ItemStack::copy)
                    .ifPresent(remainder -> remainders.set(index, remainder));
            }
        }

        return remainders;
    }

    @Override
    public List<RecipeDisplay> itematic$displays(HolderGetter<Item> items) {
        return List.of(
            new ShapelessCraftingRecipeDisplay(
                List.of(
                    this.input.display(),
                    this.material.display()
                ),
                this.result.display(),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.CRAFTING_TABLE))
            )
        );
    }
}
