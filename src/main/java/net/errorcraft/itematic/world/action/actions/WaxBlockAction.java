package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoneycombItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public record WaxBlockAction(ActionContextParameter position) implements Action<WaxBlockAction> {
    public static final Codec<WaxBlockAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(WaxBlockAction::position)
    ).apply(instance, WaxBlockAction::new));

    @Override
    public ActionType<WaxBlockAction> type() {
        return ActionTypes.WAX_BLOCK;
    }

    @Override
    public boolean execute(ActionContext context) {
        ServerWorld world = context.world();
        BlockPos pos = context.blockPos(this.position);
        return HoneycombItem.getWaxedState(world.getBlockState(pos)).map(state -> {
            PlayerEntity player = context.player(ActionContextParameter.THIS).orElse(null);
            world.setBlockState(pos, state, Block.NOTIFY_ALL_AND_REDRAW);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state));
            world.syncWorldEvent(null, WorldEvents.BLOCK_WAXED, pos, 0);
            return true;
        }).orElse(false);
    }
}
