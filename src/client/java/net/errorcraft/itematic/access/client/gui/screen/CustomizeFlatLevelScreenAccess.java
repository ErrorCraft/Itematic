package net.errorcraft.itematic.access.client.gui.screen;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;

public interface CustomizeFlatLevelScreenAccess {
    HolderLookup.RegistryLookup<Item> itematic$itemLookup();
    void itematic$setItemLookup(HolderLookup.RegistryLookup<Item> itemLookup);
}
