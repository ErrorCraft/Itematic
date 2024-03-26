package net.errorcraft.itematic.access.client.gui.screen.recipebook;

import net.minecraft.world.World;

public interface RecipeBookWidgetAccess {
    default void itematic$initializeRecipeSpecific(World world) {}
}
