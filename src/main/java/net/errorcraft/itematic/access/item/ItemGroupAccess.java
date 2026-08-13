package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryProvider;
import net.errorcraft.itematic.world.level.ItemAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ItemGroupAccess {
    default ItemStack itematic$icon(ItemAccess access) {
        return ItemStack.EMPTY;
    }
    default void itematic$setIconKey(ResourceKey<Item> iconKey) {}
    default void itematic$setEntryProviderTag(TagKey<ItemGroupEntryProvider> entryProviderTag) {}
}
