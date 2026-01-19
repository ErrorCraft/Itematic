package net.errorcraft.itematic.item.group.entry;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.group.entry.entries.StackItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.entries.TagItemGroupEntry;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.StringIdentifiable;

import java.util.function.Function;

public interface ItemGroupEntry {
    Codec<ItemGroupEntry> ENTRY_CODEC = StringIdentifiable.createCodec(ItemGroupEntryType::values).dispatch(ItemGroupEntry::type, ItemGroupEntryType::codec);
    Codec<ItemGroupEntry> CODEC = Codec.either(RegistryFixedCodec.of(RegistryKeys.ITEM), ENTRY_CODEC).xmap(either -> either.map(StackItemGroupEntry::new, Function.identity()), ItemGroupEntry::createEither);

    static ItemGroupEntry simple(RegistryEntry<Item> item) {
        return new StackItemGroupEntry(item);
    }

    static ItemGroupEntry requiresPermissions(RegistryEntry<Item> item) {
        return new StackItemGroupEntry(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, true, item, ComponentChanges.EMPTY);
    }

    static ItemGroupEntry tag(TagKey<Item> tag) {
        return new TagItemGroupEntry(tag);
    }

    ItemGroupEntryType type();
    void addStacks(ItemGroup.DisplayContext context, ItemGroup.Entries entries);
    Either<RegistryEntry<Item>, ItemGroupEntry> createEither();
}
