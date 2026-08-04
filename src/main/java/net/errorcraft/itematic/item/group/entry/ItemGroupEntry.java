package net.errorcraft.itematic.item.group.entry;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.group.entry.entries.StackItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.entries.TagItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import java.util.function.Function;

public interface ItemGroupEntry {
    Codec<ItemGroupEntry> ENTRY_CODEC = StringRepresentable.fromEnum(ItemGroupEntryType::values).dispatch(ItemGroupEntry::type, ItemGroupEntryType::codec);
    Codec<ItemGroupEntry> CODEC = Codec.either(RegistryFixedCodec.create(Registries.ITEM), ENTRY_CODEC).xmap(either -> either.map(StackItemGroupEntry::new, Function.identity()), ItemGroupEntry::createEither);

    static ItemGroupEntry simple(Holder<Item> item) {
        return new StackItemGroupEntry(item);
    }

    static ItemGroupEntry requiresPermissions(Holder<Item> item) {
        return new StackItemGroupEntry(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, true, item, DataComponentPatch.EMPTY);
    }

    static ItemGroupEntry tag(TagKey<Item> tag) {
        return new TagItemGroupEntry(tag);
    }

    ItemGroupEntryType type();
    void addStacks(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries);
    Either<Holder<Item>, ItemGroupEntry> createEither();
}
