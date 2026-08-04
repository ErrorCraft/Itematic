package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RawShapedRecipeAccess;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ShapedRecipe.class)
public abstract class ShapedRecipeExtender implements CraftingRecipe, RecipeAccess {
    @Shadow
    @Final
    ShapedRecipePattern pattern;

    @Shadow
    @Final
    ItemStack result;

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        return ((RawShapedRecipeAccess)(Object) this.pattern).itematic$remainder(input);
    }

    @Override
    public List<RecipeDisplay> itematic$displays(HolderGetter<Item> items) {
        return List.of(
            new ShapedCraftingRecipeDisplay(
                this.pattern.width(),
                this.pattern.height(),
                this.pattern.ingredients()
                    .stream()
                    .map(ingredient -> ingredient.map(Ingredient::display)
                        .orElse(SlotDisplay.Empty.INSTANCE))
                    .toList(),
                new SlotDisplay.ItemStackSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.CRAFTING_TABLE))
            )
        );
    }
}
