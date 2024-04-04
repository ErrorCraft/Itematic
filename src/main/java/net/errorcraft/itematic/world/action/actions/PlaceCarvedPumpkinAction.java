package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;

public record PlaceCarvedPumpkinAction(ActionContextParameter position) implements Action<PlaceCarvedPumpkinAction> {
    public static final MapCodec<PlaceCarvedPumpkinAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(PlaceCarvedPumpkinAction::position)
    ).apply(instance, PlaceCarvedPumpkinAction::new));

    public static PlaceCarvedPumpkinAction of(ActionContextParameter position) {
        return new PlaceCarvedPumpkinAction(position);
    }

    @Override
    public ActionType<PlaceCarvedPumpkinAction> type() {
        return ActionTypes.PLACE_CARVED_PUMPKIN;
    }

    @Override
    public boolean execute(ActionContext context) {
        ServerWorld world = context.world();
        BlockPos pos = context.blockPos(this.position);
        if (!world.isAir(pos)) {
            return false;
        }
        if (!((CarvedPumpkinBlock) Blocks.CARVED_PUMPKIN).canDispense(world, pos)) {
            return false;
        }
        world.setBlockState(pos, Blocks.CARVED_PUMPKIN.getDefaultState(), Block.NOTIFY_ALL);
        world.emitGameEvent(context.entity(ActionContextParameter.THIS).orElse(null), GameEvent.BLOCK_PLACE, pos);
        return true;
    }
}
