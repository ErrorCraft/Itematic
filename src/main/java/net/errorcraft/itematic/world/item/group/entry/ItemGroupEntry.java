package net.errorcraft.itematic.world.item.group.entry;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.group.entry.entries.ItemEntry;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public interface ItemGroupEntry<T extends ItemGroupEntry<T>> {
    Codec<ItemGroupEntry<?>> ELEMENT_CODEC = ItematicBuiltInRegistries.ITEM_GROUP_ENTRY_TYPE.byNameCodec()
        .dispatch(ItemGroupEntry::codec, Function.identity());
    Codec<ItemGroupEntry<?>> CODEC = Codec.either(ELEMENT_CODEC, Items.LIST_CODEC)
        .xmap(
            either -> either.map(
                Function.identity(),
                ItemEntry::new
            ),
            ItemGroupEntry::createEither
        );

    MapCodec<T> codec();
    void addStacks(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries);

    private static Either<ItemGroupEntry<?>, HolderSet<Item>> createEither(ItemGroupEntry<?> entry) {
        if (entry instanceof ItemEntry item) {
            return item.createEither();
        }

        return Either.left(entry);
    }
}
