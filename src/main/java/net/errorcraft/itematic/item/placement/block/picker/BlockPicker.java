package net.errorcraft.itematic.item.placement.block.picker;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import org.jetbrains.annotations.Nullable;

public interface BlockPicker<T extends BlockPicker<T>> {
    Codec<BlockPicker<?>> ELEMENT_CODEC = ItematicRegistries.BLOCK_PICKER_TYPE.getCodec().dispatch("type", BlockPicker::type, BlockPickerType::codec);
    Codec<BlockPicker<?>> CODEC = Codec.lazyInitialized(() -> Codec.either(ELEMENT_CODEC, RegistryFixedCodec.of(RegistryKeys.BLOCK)).xmap(either -> either.map(modifier -> modifier, SimpleBlockPicker::new), modifier -> {
        if (modifier instanceof SimpleBlockPicker simpleModifier) {
            return Either.right(simpleModifier.block());
        }
        return Either.left(modifier);
    }));

    BlockPickerType<T> type();
    RegistryEntry<Block> defaultBlock();
    @Nullable
    BlockState placementState(ItemPlacementContext context);
    default ItemPlacementContext placementContext(ItemPlacementContext context) {
        return this.defaultBlock().value().itematic$placementContext(context);
    }
}
