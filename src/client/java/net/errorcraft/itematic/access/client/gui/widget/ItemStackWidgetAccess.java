package net.errorcraft.itematic.access.client.gui.widget;

import net.minecraft.item.ItemStack;

public interface ItemStackWidgetAccess {
    default void itematic$setStack(ItemStack stack) {}
}
