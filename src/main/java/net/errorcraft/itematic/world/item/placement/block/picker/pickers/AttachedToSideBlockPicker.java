package net.errorcraft.itematic.world.item.placement.block.picker.pickers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPickerType;
import net.errorcraft.itematic.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public record AttachedToSideBlockPicker(Holder<Block> attachedBlock, Holder<Block> otherBlock, Direction attachedSide) implements BlockPicker<AttachedToSideBlockPicker> {
    public static final MapCodec<AttachedToSideBlockPicker> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Blocks.CODEC.fieldOf("attached_block").forGetter(AttachedToSideBlockPicker::attachedBlock),
        Blocks.CODEC.fieldOf("other_block").forGetter(AttachedToSideBlockPicker::otherBlock),
        Direction.CODEC.fieldOf("attached_side").forGetter(AttachedToSideBlockPicker::attachedSide)
    ).apply(instance, AttachedToSideBlockPicker::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, AttachedToSideBlockPicker> STREAM_CODEC = StreamCodec.composite(
        Blocks.STREAM_CODEC, AttachedToSideBlockPicker::attachedBlock,
        Blocks.STREAM_CODEC, AttachedToSideBlockPicker::otherBlock,
        Direction.STREAM_CODEC, AttachedToSideBlockPicker::attachedSide,
        AttachedToSideBlockPicker::new
    );

    @Override
    public BlockPickerType<AttachedToSideBlockPicker> type() {
        return BlockPickerType.ATTACHED_TO_SIDE;
    }

    @Override
    public Holder<Block> defaultBlock() {
        return this.attachedBlock;
    }

    @Override
    public @Nullable BlockState placementState(BlockPlaceContext context) {
        BlockState state = this.otherBlock.value().getStateForPlacement(context);
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction == this.attachedSide.getOpposite()) {
                continue;
            }
            BlockState actualState = direction == this.attachedSide ? this.attachedBlock.value().getStateForPlacement(context) : state;
            if (actualState != null && actualState.canSurvive(level, pos)) {
                return actualState;
            }
        }
        return null;
    }
}
