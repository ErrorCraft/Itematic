package net.errorcraft.itematic.access.client.gui.screen;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface GameModeSwitcherScreenAccess {
    interface GameModeSelectionAccess {
        default ItemStack itematic$icon(Registry<Item> registry) {
            return ItemStack.EMPTY;
        }
        default void itematic$setIcon(ResourceKey<Item> item) {}
    }
}
