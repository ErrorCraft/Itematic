package net.errorcraft.itematic.access.client.gui.screens.debug;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface GameModeSwitcherScreenAccess {
    interface GameModeIconAccess {
        default ItemStack itematic$icon(Registry<Item> items) {
            return ItemStack.EMPTY;
        }
        default void itematic$setIcon(ResourceKey<Item> item) {}
    }
}
