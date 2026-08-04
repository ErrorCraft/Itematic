package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

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
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        Level world = context.world();
        if (!world.isEmptyBlock(pos)) {
            return false;
        }

        if (!((CarvedPumpkinBlock) Blocks.CARVED_PUMPKIN).canSpawnGolem(world, pos)) {
            return false;
        }

        world.setBlock(pos, Blocks.CARVED_PUMPKIN.defaultBlockState(), Block.UPDATE_ALL);
        world.gameEvent(context.get(LootContextParams.THIS_ENTITY), GameEvent.BLOCK_PLACE, pos);
        return true;
    }
}
