package net.errorcraft.itematic.world.item.placement.block.picker;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface BlockPicker<T extends BlockPicker<T>> {
    Codec<BlockPicker<?>> ELEMENT_CODEC = ItematicRegistries.BLOCK_PICKER_TYPE.byNameCodec()
        .dispatch(BlockPicker::type, BlockPickerType::codec);
    Codec<BlockPicker<?>> CODEC = Codec.lazyInitialized(() -> Codec.either(ELEMENT_CODEC, RegistryFixedCodec.create(Registries.BLOCK))
        .xmap(
            either -> either.map(
                Function.identity(),
                SimpleBlockPicker::new
            ),
            modifier -> {
                if (modifier instanceof SimpleBlockPicker(Holder<Block> block)) {
                    return Either.right(block);
                }

                return Either.left(modifier);
            }
        )
    );

    BlockPickerType<T> type();
    Holder<Block> defaultBlock();
    @Nullable
    BlockState placementState(BlockPlaceContext context);
    default BlockPlaceContext placementContext(BlockPlaceContext context) {
        return this.defaultBlock().value().itematic$blockPlaceContext(context);
    }
}
