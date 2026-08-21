package net.errorcraft.itematic.access.client.gui.screens;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;

public interface CreateFlatWorldScreenAccess {
    default HolderLookup.RegistryLookup<Item> itematic$items() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setItems(HolderLookup.RegistryLookup<Item> items) {}

    interface DetailsListAccess {
        default HolderLookup.RegistryLookup<Item> itematic$items() {
            throw new AssertionError("Implemented via mixin");
        }
    }
}
