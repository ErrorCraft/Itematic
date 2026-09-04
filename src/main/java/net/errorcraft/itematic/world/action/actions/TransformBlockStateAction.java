package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record TransformBlockStateAction(PositionTarget position, BlockStateProvider provider, boolean pushEntitiesUpwards) implements Action<TransformBlockStateAction> {
    public static final MapCodec<TransformBlockStateAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(TransformBlockStateAction::position),
        BlockStateProvider.CODEC.fieldOf("provider").forGetter(TransformBlockStateAction::provider),
        Codec.BOOL.optionalFieldOf("push_entities_upwards", false).forGetter(TransformBlockStateAction::pushEntitiesUpwards)
    ).apply(instance, TransformBlockStateAction::new));

    public static TransformBlockStateAction of(PositionTarget position, BlockStateProvider provider) {
        return new TransformBlockStateAction(position, provider, false);
    }

    public static TransformBlockStateAction ofPushingUpwards(PositionTarget position, BlockStateProvider provider) {
        return new TransformBlockStateAction(position, provider, true);
    }

    @Override
    public ActionType<TransformBlockStateAction> type() {
        return ActionType.TRANSFORM_BLOCK_STATE;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        if (!(context.level() instanceof ServerLevel level)) {
            return false;
        }

        BlockState currentBlockState = level.getBlockState(pos);
        BlockState newBlockState = this.provider.getOptionalState(level, context.level().getRandom(), pos);
        if (newBlockState == null) {
            return false;
        }

        if (this.pushEntitiesUpwards) {
            Block.pushEntitiesUp(currentBlockState, newBlockState, level, pos);
        }

        level.setBlockAndUpdate(pos, newBlockState);
        return true;
    }
}
