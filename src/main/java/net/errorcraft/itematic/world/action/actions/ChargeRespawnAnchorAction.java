package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public record ChargeRespawnAnchorAction(ActionContextParameter position) implements Action<ChargeRespawnAnchorAction> {
    public static final MapCodec<ChargeRespawnAnchorAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(ChargeRespawnAnchorAction::position)
    ).apply(instance, ChargeRespawnAnchorAction::new));

    public static ChargeRespawnAnchorAction of(ActionContextParameter position) {
        return new ChargeRespawnAnchorAction(position);
    }

    @Override
    public ActionType<ChargeRespawnAnchorAction> type() {
        return ActionTypes.CHARGE_RESPAWN_ANCHOR;
    }

    @Override
    public boolean execute(ActionContext context) {
        ServerWorld world = context.world();
        BlockPos pos = context.blockPos(this.position);
        BlockState state = world.getBlockState(pos);
        if (!state.isOf(Blocks.RESPAWN_ANCHOR)) {
            return false;
        }
        if (state.get(RespawnAnchorBlock.CHARGES) == RespawnAnchorBlock.MAX_CHARGES) {
            return false;
        }
        RespawnAnchorBlock.charge(context.entity(ActionContextParameter.THIS).orElse(null), world, pos, state);
        return true;
    }
}
