package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.util.math.BlockPos;

public record TakeHoneyAction(ActionContextParameter position) implements Action<TakeHoneyAction> {
    public static final MapCodec<TakeHoneyAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(TakeHoneyAction::position)
    ).apply(instance, TakeHoneyAction::new));

    public static TakeHoneyAction of(ActionContextParameter position) {
        return new TakeHoneyAction(position);
    }

    @Override
    public ActionType<TakeHoneyAction> type() {
        return ActionTypes.TAKE_HONEY;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.blockPos(this.position);
        BlockState state = context.world().getBlockState(pos);
        if (!state.contains(BeehiveBlock.HONEY_LEVEL)) {
            return false;
        }
        if (state.get(BeehiveBlock.HONEY_LEVEL) < BeehiveBlock.FULL_HONEY_LEVEL) {
            return false;
        }
        if (!(state.getBlock() instanceof BeehiveBlock beehiveBlock)) {
            return false;
        }
        beehiveBlock.takeHoney(context.world(), state, pos, context.player(ActionContextParameter.THIS).orElse(null), BeehiveBlockEntity.BeeState.BEE_RELEASED);
        return true;
    }
}
