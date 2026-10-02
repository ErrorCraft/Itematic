package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record TransformBlockStateAction(PositionTarget position, Holder<BlockStateProvider> provider, BlockTransformer.TransformType transformType, BlockTransformer.TransformParticle particle, boolean pushEntitiesUpwards) implements Action<TransformBlockStateAction> {
    public static final MapCodec<TransformBlockStateAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(TransformBlockStateAction::position),
        BlockStateProvider.CODEC.fieldOf("provider").forGetter(TransformBlockStateAction::provider),
        BlockTransformer.TransformType.CODEC.optionalFieldOf("transform_type", BlockTransformer.TransformType.SINGLE_BLOCK).forGetter(TransformBlockStateAction::transformType),
        BlockTransformer.TransformParticle.CODEC.optionalFieldOf("particle", BlockTransformer.TransformParticle.NONE).forGetter(TransformBlockStateAction::particle),
        Codec.BOOL.optionalFieldOf("push_entities_upwards", false).forGetter(TransformBlockStateAction::pushEntitiesUpwards)
    ).apply(instance, TransformBlockStateAction::new));

    public static TransformBlockStateAction of(PositionTarget position, BlockStateProvider provider) {
        return new TransformBlockStateAction(
            position,
            Holder.direct(provider),
            BlockTransformer.TransformType.SINGLE_BLOCK,
            BlockTransformer.TransformParticle.NONE,
            false
        );
    }

    public static TransformBlockStateAction of(PositionTarget position, BlockStateProvider provider, BlockTransformer.TransformType transformType, BlockTransformer.TransformParticle particle) {
        return new TransformBlockStateAction(
            position,
            Holder.direct(provider),
            transformType,
            particle,
            false
        );
    }

    public static TransformBlockStateAction ofPushingUpwards(PositionTarget position, BlockStateProvider provider) {
        return new TransformBlockStateAction(
            position,
            Holder.direct(provider),
            BlockTransformer.TransformType.SINGLE_BLOCK,
            BlockTransformer.TransformParticle.NONE,
            true
        );
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

        Level level = context.level();
        BlockState currentBlockState = level.getBlockState(pos);
        BlockState newBlockState = this.provider.value().getOptionalState(level, context.level().getRandom(), pos);
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
