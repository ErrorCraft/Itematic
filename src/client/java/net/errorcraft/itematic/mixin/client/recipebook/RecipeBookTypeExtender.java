package net.errorcraft.itematic.mixin.client.recipebook;

import net.errorcraft.itematic.recipe.book.ItematicRecipeBookCategories;
import net.minecraft.client.recipebook.RecipeBookType;
import net.minecraft.recipe.book.RecipeBookCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RecipeBookType.class)
public enum RecipeBookTypeExtender {
    ITEMATIC_BREWING(ItematicRecipeBookCategories.BREWING_MODIFY, ItematicRecipeBookCategories.BREWING_AMPLIFY);

    @Shadow
    RecipeBookTypeExtender(RecipeBookCategory... categories) {}
}
