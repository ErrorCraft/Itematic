package net.errorcraft.itematic.world.item.group.entry;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.item.group.entry.entries.StackItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public interface ItemGroupEntry<T extends ItemGroupEntry<T>> {
    Codec<ItemGroupEntry<?>> ELEMENT_CODEC = ItematicBuiltInRegistries.ITEM_GROUP_ENTRY_TYPE.byNameCodec()
        .dispatch(ItemGroupEntry::type, ItemGroupEntryType::codec);
    Codec<ItemGroupEntry<?>> CODEC = Codec.either(ELEMENT_CODEC, Item.CODEC)
        .xmap(
            either -> either.map(
                Function.identity(),
                StackItemGroupEntry::new
            ),
            ItemGroupEntry::createEither
        );

    ItemGroupEntryType<T> type();
    void addStacks(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries);
    Either<ItemGroupEntry<?>, Holder<Item>> createEither();
}
