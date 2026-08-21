package net.errorcraft.itematic.access.client.gui.components;

import net.minecraft.world.item.ItemStack;

public interface ItemDisplayWidgetAccess {
    default void itematic$setStack(ItemStack stack) {}
}
