package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;

public record SetBlockStateAction(PositionTarget position, BlockState state) implements Action<SetBlockStateAction> {
    public static final MapCodec<SetBlockStateAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(SetBlockStateAction::position),
        BlockState.CODEC.fieldOf("state").forGetter(SetBlockStateAction::state)
    ).apply(instance, SetBlockStateAction::new));

    public static SetBlockStateAction of(PositionTarget position, RegistryEntry<Block> entry) {
        return new SetBlockStateAction(position, entry.value().getDefaultState());
    }

    @Override
    public ActionType<SetBlockStateAction> type() {
        return ActionTypes.SET_BLOCK_STATE;
    }

    @Override
    public boolean execute(NewActionContext context) {
        ServerWorld world = context.world();
        BlockPos pos = context.getBlockPos(this.position.parameter());
        if (pos == null) {
            return false;
        }

        if (!world.setBlockState(pos, this.state, Block.NOTIFY_ALL_AND_REDRAW)) {
            return false;
        }

        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(this.state));
        return true;
    }
}
