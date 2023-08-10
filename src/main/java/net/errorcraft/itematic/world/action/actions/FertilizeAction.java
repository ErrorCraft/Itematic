package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.item.BoneMealItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldEvents;

public record FertilizeAction() implements Action {
    public static final FertilizeAction INSTANCE = new FertilizeAction();
    public static final Codec<FertilizeAction> CODEC = Codec.unit(INSTANCE);

    @Override
    public ActionType<?> type() {
        return ActionTypes.FERTILIZE;
    }

    @Override
    public boolean execute(ActionContext context) {
        ServerWorld world = context.world();
        BlockPos pos = context.blockPos();
        if (BoneMealItem.useOnFertilizable(context.stack(), world, pos)) {
            world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, pos, 0);
            return true;
        }
        Direction side = context.side();
        BlockPos offsetPos = pos.offset(side);
        if (world.getBlockState(pos).isSideSolidFullSquare(world, pos, side) && BoneMealItem.useOnGround(context.stack(), world, offsetPos, side)) {
            world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, offsetPos, 0);
            return true;
        }
        return false;
    }
}
