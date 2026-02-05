package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.block.dispenser.ShearsDispenserBehaviorAccessor;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public record ShearAtPositionAction(ActionContextParameter position) implements Action<ShearAtPositionAction> {
    public static final MapCodec<ShearAtPositionAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(ShearAtPositionAction::position)
    ).apply(instance, ShearAtPositionAction::new));

    public static ShearAtPositionAction of(ActionContextParameter position) {
        return new ShearAtPositionAction(position);
    }

    @Override
    public ActionType<ShearAtPositionAction> type() {
        return ActionTypes.SHEAR_AT_POSITION;
    }

    @Override
    public boolean execute(ActionContext context) {
        ServerWorld world = context.world();
        BlockPos pos = context.blockPos(this.position);
        return ShearsDispenserBehaviorAccessor.tryShearBlock(world, pos) || ShearsDispenserBehaviorAccessor.tryShearEntity(world, pos, context.stack());
    }
}
