package net.errorcraft.itematic.recipe.book;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.mixin.stats.RecipeBookSettingsAccessor;
import net.minecraft.stats.RecipeBookSettings;

public class ItematicRecipeBookOptions {
    public static final MapCodec<RecipeBookSettings.TypeSettings> BREWING_CODEC = RecipeBookSettingsAccessor.TypeSettingsAccessor.codec(
        "isBrewingStandGuiOpen",
        "isBrewingStandFilteringCraftable"
    );

    private ItematicRecipeBookOptions() {}
}
