package net.errorcraft.itematic.access.world;

import net.errorcraft.itematic.item.ItemAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public interface WorldViewAccess {
    default ItemAccess itematic$getItemAccess() {
        return null;
    }
    default RegistryEntry<Item> itematic$getItem(RegistryKey<Item> key) {
        return null;
    }
    default ItemStack itematic$createStack(RegistryKey<Item> key) {
        return this.itematic$getItemAccess().getOptionalEntry(key)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
    }
    default ItemStack itematic$createStack(RegistryKey<Item> key, int count) {
        return this.itematic$getItemAccess().getOptionalEntry(key)
            .map(entry -> new ItemStack(entry, count))
            .orElse(ItemStack.EMPTY);
    }
}
