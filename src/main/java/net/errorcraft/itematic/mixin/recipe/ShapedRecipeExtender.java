package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RawShapedRecipeAccess;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.ShapedCraftingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ShapedRecipe.class)
public abstract class ShapedRecipeExtender implements CraftingRecipe, RecipeAccess {
    @Shadow
    @Final
    RawShapedRecipe raw;

    @Shadow
    @Final
    ItemStack result;

    @Override
    public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput input) {
        return ((RawShapedRecipeAccess)(Object) this.raw).itematic$remainder(input);
    }

    @Override
    public List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        return List.of(
            new ShapedCraftingRecipeDisplay(
                this.raw.getWidth(),
                this.raw.getHeight(),
                this.raw.getIngredients()
                    .stream()
                    .map(ingredient -> ingredient.map(Ingredient::toDisplay)
                        .orElse(SlotDisplay.EmptySlotDisplay.INSTANCE))
                    .toList(),
                new SlotDisplay.StackSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.CRAFTING_TABLE))
            )
        );
    }
}
