package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.item.BoneMealItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;

public record FertilizeAction(PositionTarget position) implements Action<FertilizeAction> {
    public static final MapCodec<FertilizeAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(FertilizeAction::position)
    ).apply(instance, FertilizeAction::new));

    public static FertilizeAction of(PositionTarget position) {
        return new FertilizeAction(position);
    }

    @Override
    public ActionType<FertilizeAction> type() {
        return ActionTypes.FERTILIZE;
    }

    @Override
    public boolean execute(ActionContext context) {
        Vec3d pos = context.get(this.position.parameter());
        if (pos == null) {
            return false;
        }

        BlockPos blockPos = BlockPos.ofFloored(pos);
        ServerWorld world = context.world();
        if (BoneMealItem.useOnFertilizable(null, world, blockPos)) {
            fertilized(world, blockPos);
            return true;
        }

        Direction side = context.get(ItematicContextParameters.SIDE);
        if (side == null) {
            return false;
        }

        BlockPos offsetBlockPos = blockPos.offset(side);
        if (world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, side) && BoneMealItem.useOnGround(null, world, offsetBlockPos, side)) {
            fertilized(world, offsetBlockPos);
            return true;
        }

        return false;
    }

    private static void fertilized(ServerWorld world, BlockPos pos) {
        world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, pos, 15);
    }
}
