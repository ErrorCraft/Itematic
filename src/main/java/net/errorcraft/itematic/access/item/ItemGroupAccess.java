package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.item.ItemAccess;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;

public interface ItemGroupAccess {
    default ItemStack itematic$icon(ItemAccess access) {
        return ItemStack.EMPTY;
    }
    default void itematic$setIconKey(RegistryKey<Item> iconKey) {}
    default void itematic$setEntryProviderTag(TagKey<ItemGroupEntryProvider> entryProviderTag) {}
}
