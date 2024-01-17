package net.errorcraft.itematic.access.client.gui.screen;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;

public interface GameModeSelectionScreenGameModeSelectionAccess {
    default ItemStack itematic$icon(Registry<Item> registry) {
        return ItemStack.EMPTY;
    }
}
