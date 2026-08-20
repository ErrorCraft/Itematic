package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public record SetBlockStateAction(PositionTarget position, BlockState state) implements Action<SetBlockStateAction> {
    public static final MapCodec<SetBlockStateAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(SetBlockStateAction::position),
        BlockState.CODEC.fieldOf("state").forGetter(SetBlockStateAction::state)
    ).apply(instance, SetBlockStateAction::new));

    public static SetBlockStateAction of(PositionTarget position, Holder<Block> entry) {
        return new SetBlockStateAction(position, entry.value().defaultBlockState());
    }

    @Override
    public ActionType<SetBlockStateAction> type() {
        return ActionType.SET_BLOCK_STATE;
    }

    @Override
    public boolean execute(ActionContext context) {
        Level level = context.level();
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        if (!level.setBlock(pos, this.state, Block.UPDATE_ALL_IMMEDIATE)) {
            return false;
        }

        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(this.state));
        return true;
    }
}
