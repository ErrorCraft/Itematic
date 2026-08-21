package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record ChargeRespawnAnchorAction(PositionTarget position) implements Action<ChargeRespawnAnchorAction> {
    public static final MapCodec<ChargeRespawnAnchorAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(ChargeRespawnAnchorAction::position)
    ).apply(instance, ChargeRespawnAnchorAction::new));

    public static ChargeRespawnAnchorAction of(PositionTarget position) {
        return new ChargeRespawnAnchorAction(position);
    }

    @Override
    public ActionType<ChargeRespawnAnchorAction> type() {
        return ActionType.CHARGE_RESPAWN_ANCHOR;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.get(this.position.contextParam(), BlockPos::containing);
        if (pos == null) {
            return false;
        }

        Level level = context.level();
        BlockState state = level.getBlockState(pos);
        if (!state.is(Blocks.RESPAWN_ANCHOR)) {
            return false;
        }

        if (state.getValue(RespawnAnchorBlock.CHARGE) == RespawnAnchorBlock.MAX_CHARGES) {
            return false;
        }

        RespawnAnchorBlock.charge(context.get(LootContextParams.THIS_ENTITY), level, pos, state);
        return true;
    }
}
