package net.errorcraft.itematic.access.world;

import net.errorcraft.itematic.item.ItemAccess;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface WorldViewAccess {
    default ItemAccess itematic$getItemAccess() {
        return null;
    }
    default Holder<Item> itematic$getItem(ResourceKey<Item> key) {
        return null;
    }
    default ItemStack itematic$createStack(ResourceKey<Item> key) {
        return this.itematic$getItemAccess().getOptionalEntry(key)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
    }
    default ItemStack itematic$createStack(ResourceKey<Item> key, int count) {
        return this.itematic$getItemAccess().getOptionalEntry(key)
            .map(entry -> new ItemStack(entry, count))
            .orElse(ItemStack.EMPTY);
    }
}
