package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.HoneycombItem;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public record WaxBlockAction(PositionTarget position) implements Action<WaxBlockAction> {
    public static final MapCodec<WaxBlockAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(WaxBlockAction::position)
    ).apply(instance, WaxBlockAction::new));

    public static WaxBlockAction of(PositionTarget position) {
        return new WaxBlockAction(position);
    }

    @Override
    public ActionType<WaxBlockAction> type() {
        return ActionTypes.WAX_BLOCK;
    }

    @Override
    public boolean execute(ActionContext context) {
        return false;
    }

    @Override
    public boolean execute(NewActionContext context) {
        ServerWorld world = context.world();
        BlockPos pos = context.getBlockPos(this.position.parameter());
        return HoneycombItem.getWaxedState(world.getBlockState(pos))
            .map(state -> {
                Entity entity = context.get(LootContextParameters.THIS_ENTITY);
                world.setBlockState(pos, state, Block.NOTIFY_ALL_AND_REDRAW);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(entity, state));
                world.syncWorldEvent(WorldEvents.BLOCK_WAXED, pos, 0);
                return true;
            })
            .orElse(false);
    }
}
