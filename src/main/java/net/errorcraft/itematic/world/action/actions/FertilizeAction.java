package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.Vec3;

public record FertilizeAction(PositionTarget position) implements Action<FertilizeAction> {
    public static final MapCodec<FertilizeAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(FertilizeAction::position)
    ).apply(instance, FertilizeAction::new));

    public static FertilizeAction of(PositionTarget position) {
        return new FertilizeAction(position);
    }

    @Override
    public ActionType<FertilizeAction> type() {
        return ActionType.FERTILIZE;
    }

    @Override
    public boolean execute(ActionContext context) {
        Vec3 pos = context.get(this.position.contextParam());
        if (pos == null) {
            return false;
        }

        BlockPos blockPos = BlockPos.containing(pos);
        Level world = context.level();
        if (BoneMealItem.growCrop(null, world, blockPos)) {
            fertilized(world, blockPos);
            return true;
        }

        Direction side = context.get(ItematicContextKeys.SIDE);
        if (side == null) {
            return false;
        }

        BlockPos offsetBlockPos = blockPos.relative(side);
        if (world.getBlockState(blockPos).isFaceSturdy(world, blockPos, side) && BoneMealItem.growWaterPlant(null, world, offsetBlockPos, side)) {
            fertilized(world, offsetBlockPos);
            return true;
        }

        return false;
    }

    private static void fertilized(Level world, BlockPos pos) {
        world.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 15);
    }
}
