package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public record PlaceCarvedPumpkinAction(PositionTarget position) implements Action<PlaceCarvedPumpkinAction> {
    public static final MapCodec<PlaceCarvedPumpkinAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(PlaceCarvedPumpkinAction::position)
    ).apply(instance, PlaceCarvedPumpkinAction::new));

    public static PlaceCarvedPumpkinAction of(PositionTarget position) {
        return new PlaceCarvedPumpkinAction(position);
    }

    @Override
    public ActionType<PlaceCarvedPumpkinAction> type() {
        return ActionTypes.PLACE_CARVED_PUMPKIN;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.getBlockPos(this.position.parameter());
        if (pos == null) {
            return false;
        }

        World world = context.world();
        if (!world.isAir(pos)) {
            return false;
        }

        if (!((CarvedPumpkinBlock) Blocks.CARVED_PUMPKIN).canDispense(world, pos)) {
            return false;
        }

        world.setBlockState(pos, Blocks.CARVED_PUMPKIN.getDefaultState(), Block.NOTIFY_ALL);
        world.emitGameEvent(context.get(LootContextParameters.THIS_ENTITY), GameEvent.BLOCK_PLACE, pos);
        return true;
    }
}
