package net.errorcraft.itematic.access.world.level;

import net.errorcraft.itematic.world.level.ItemAccess;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface LevelReaderAccess {
    default ItemAccess itematic$itemAccess() {
        throw new AssertionError("Implemented via mixin");
    }
    default Holder<Item> itematic$getItem(ResourceKey<Item> key) {
        return this.itematic$itemAccess().getOrThrow(key);
    }
    default ItemStack itematic$createStack(ResourceKey<Item> key) {
        return this.itematic$itemAccess().get(key)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
    }
    default ItemStack itematic$createStack(ResourceKey<Item> key, int count) {
        return this.itematic$itemAccess().get(key)
            .map(entry -> new ItemStack(entry, count))
            .orElse(ItemStack.EMPTY);
    }
}
