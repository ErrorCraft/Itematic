package net.errorcraft.itematic.mm.client;

import com.chocohead.mm.api.ClassTinkerers;
import net.errorcraft.itematic.recipe.book.ItematicRecipeBookCategories;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.recipe.book.RecipeBookCategory;

public class ItematicClientEarlyRiser implements Runnable {
    @Override
    public void run() {
        FabricLoader loader = FabricLoader.getInstance();

        // There is no early riser client entry point definition, so this check is done in main instead
        if (loader.getEnvironmentType() != EnvType.CLIENT) {
            return;
        }

        MappingResolver remapper = loader.getMappingResolver();
        String recipeBookType = remapper.mapClassName("intermediary", "net.minecraft.class_10331");
        ClassTinkerers.enumBuilder(recipeBookType, RecipeBookCategory[].class)
            .addEnum("ITEMATIC$BREWING", () -> new Object[] {
                new RecipeBookCategory[] {
                    ItematicRecipeBookCategories.BREWING_MODIFY,
                    ItematicRecipeBookCategories.BREWING_AMPLIFY
                }
            })
            .build();
    }
}
