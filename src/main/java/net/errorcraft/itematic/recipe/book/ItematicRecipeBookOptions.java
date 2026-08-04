package net.errorcraft.itematic.recipe.book;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.mixin.recipe.book.RecipeBookOptionsAccessor;
import net.minecraft.stats.RecipeBookSettings;

public class ItematicRecipeBookOptions {
    public static final MapCodec<RecipeBookSettings.TypeSettings> BREWING_CODEC = RecipeBookOptionsAccessor.CategoryOptionAccessor.createCodec(
        "isBrewingStandGuiOpen",
        "isBrewingStandFilteringCraftable"
    );

    private ItematicRecipeBookOptions() {}
}
