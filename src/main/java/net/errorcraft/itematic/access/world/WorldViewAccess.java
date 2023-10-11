package net.errorcraft.itematic.access.world;

import net.errorcraft.itematic.item.ItemAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public interface WorldViewAccess {
    default ItemAccess getItemAccess() {
        return null;
    }
    default RegistryEntry<Item> getItem(RegistryKey<Item> key) {
        return null;
    }
    default ItemStack createStack(RegistryKey<Item> key) {
        return this.getItemAccess().getOptionalEntry(key)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
    }
}
