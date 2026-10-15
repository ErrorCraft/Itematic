package net.errorcraft.itematic.world.item.placement.block.picker;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.errorcraft.itematic.world.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.errorcraft.itematic.world.level.block.Blocks;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface BlockPicker<T extends BlockPicker<T>> {
    Codec<BlockPicker<?>> FULL_CODEC = ItematicBuiltInRegistries.BLOCK_PICKER_TYPE.byNameCodec()
        .dispatch(BlockPicker::type, BlockPickerType::codec);
    Codec<BlockPicker<?>> CODEC = Codec.lazyInitialized(() -> ItematicCodecs.withAlternative(
        FULL_CODEC,
        Blocks.CODEC,
        SimpleBlockPicker::new,
        BlockPicker::wrap
    ));
    StreamCodec<RegistryFriendlyByteBuf, BlockPicker<?>> STREAM_CODEC = ByteBufCodecs.registry(ItematicRegistries.BLOCK_PICKER_TYPE)
        .dispatch(BlockPicker::type, BlockPickerType::streamCodec);

    BlockPickerType<T> type();
    Holder<Block> defaultBlock();
    @Nullable
    BlockState placementState(BlockPlaceContext context);
    default BlockPlaceContext placeContext(BlockPlaceContext context) {
        return this.defaultBlock().value().itematic$blockPlaceContext(context);
    }

    private static Either<BlockPicker<?>, Holder<Block>> wrap(BlockPicker<?> modifier) {
        if (modifier instanceof SimpleBlockPicker(Holder<Block> block)) {
            return Either.right(block);
        }

        return Either.left(modifier);
    }
}
