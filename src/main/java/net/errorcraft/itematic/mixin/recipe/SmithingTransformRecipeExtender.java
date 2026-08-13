package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.item.crafting.TransmuteResult;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
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
    Ingredient base;

    @Shadow
    @Final
    Optional<Ingredient> addition;

    @Shadow
    @Final
    TransmuteResult result;

    @Override
    public List<RecipeDisplay> itematic$displays(HolderGetter<Item> items) {
        return List.of(
            new SmithingRecipeDisplay(
                Ingredient.optionalIngredientToDisplay(this.template),
                this.base.display(),
                Ingredient.optionalIngredientToDisplay(this.addition),
                this.result.display(),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemIds.SMITHING_TABLE))
            )
        );
    }
}
