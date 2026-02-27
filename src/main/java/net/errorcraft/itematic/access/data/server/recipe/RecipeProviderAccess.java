package net.errorcraft.itematic.access.data.server.recipe;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.registry.RegistryWrapper;

public interface RecipeProviderAccess {
    void itematic$generate(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter exporter);
}
