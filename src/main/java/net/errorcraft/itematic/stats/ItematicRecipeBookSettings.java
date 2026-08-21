package net.errorcraft.itematic.stats;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.mixin.stats.RecipeBookSettingsAccessor;
import net.minecraft.stats.RecipeBookSettings;

public class ItematicRecipeBookSettings {
    public static final MapCodec<RecipeBookSettings.TypeSettings> BREWING_MAP_CODEC = RecipeBookSettingsAccessor.TypeSettingsAccessor.codec(
        "isBrewingStandGuiOpen",
        "isBrewingStandFilteringCraftable"
    );

    private ItematicRecipeBookSettings() {}
}
