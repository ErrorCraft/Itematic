package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingTransformRecipe;
import net.minecraft.recipe.TransmuteRecipeResult;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SmithingRecipeDisplay;
import net.minecraft.registry.RegistryEntryLookup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

@Mixin(SmithingTransformRecipe.class)
public class SmithingTransformRecipeExtender implements RecipeAccess {
    @Shadow
    @Final
    Optional<Ingredient> template;

    @Shadow
    @Final
    Optional<Ingredient> base;

    @Shadow
    @Final
    Optional<Ingredient> addition;

    @Shadow
    @Final
    TransmuteRecipeResult result;

    @Override
    public List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        return List.of(
            new SmithingRecipeDisplay(
                Ingredient.toDisplay(this.template),
                Ingredient.toDisplay(this.base),
                Ingredient.toDisplay(this.addition),
                this.result.createSlotDisplay(),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.SMITHING_TABLE))
            )
        );
    }
}
