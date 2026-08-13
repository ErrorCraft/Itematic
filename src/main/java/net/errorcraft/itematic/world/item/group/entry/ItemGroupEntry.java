package net.errorcraft.itematic.world.item.group.entry;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.item.group.entry.entries.StackItemGroupEntry;
import net.errorcraft.itematic.world.item.group.entry.entries.TagItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public interface ItemGroupEntry<T extends ItemGroupEntry<T>> {
    Codec<ItemGroupEntry<?>> ELEMENT_CODEC = ItematicRegistries.ITEM_GROUP_ENTRY_TYPE.byNameCodec()
        .dispatch(ItemGroupEntry::type, ItemGroupEntryType::codec);
    Codec<ItemGroupEntry<?>> CODEC = Codec.either(ELEMENT_CODEC, RegistryFixedCodec.create(Registries.ITEM))
        .xmap(
            either -> either.map(
                Function.identity(),
                StackItemGroupEntry::new
            ),
            ItemGroupEntry::createEither
        );

    static StackItemGroupEntry simple(Holder<Item> item) {
        return new StackItemGroupEntry(item);
    }

    static StackItemGroupEntry simple(Holder<Item> item, DataComponentPatch components) {
        return new StackItemGroupEntry(item, components);
    }

    static StackItemGroupEntry requiresPermissions(Holder<Item> item) {
        return new StackItemGroupEntry(
            CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
            true,
            item,
            DataComponentPatch.EMPTY
        );
    }

    static TagItemGroupEntry tag(TagKey<Item> tag) {
        return new TagItemGroupEntry(tag);
    }

    ItemGroupEntryType<T> type();
    void addStacks(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries);
    Either<ItemGroupEntry<?>, Holder<Item>> createEither();
}
