package net.errorcraft.itematic.mixin.client.recipebook;

import net.errorcraft.itematic.world.item.crafting.ItematicRecipeBookCategories;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SearchRecipeBookCategory.class)
public enum RecipeBookTypeExtender {
    ITEMATIC_BREWING(ItematicRecipeBookCategories.BREWING_MODIFY, ItematicRecipeBookCategories.BREWING_AMPLIFY);

    @Shadow
    RecipeBookTypeExtender(RecipeBookCategory... categories) {}
}
