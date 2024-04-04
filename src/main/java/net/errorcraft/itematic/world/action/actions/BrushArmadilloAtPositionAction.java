package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.passive.ArmadilloEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public record BrushArmadilloAtPositionAction(ActionContextParameter position) implements Action<BrushArmadilloAtPositionAction> {
    public static final MapCodec<BrushArmadilloAtPositionAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(BrushArmadilloAtPositionAction::position)
    ).apply(instance, BrushArmadilloAtPositionAction::new));

    public static BrushArmadilloAtPositionAction of(ActionContextParameter position) {
        return new BrushArmadilloAtPositionAction(position);
    }

    @Override
    public ActionType<BrushArmadilloAtPositionAction> type() {
        return ActionTypes.BRUSH_ARMADILLO_AT_POSITION;
    }

    @Override
    public boolean execute(ActionContext context) {
        ServerWorld world = context.world();
        BlockPos pos = context.blockPos(this.position);
        List<ArmadilloEntity> armadillos = world.getEntitiesByClass(ArmadilloEntity.class, new Box(pos), EntityPredicates.EXCEPT_SPECTATOR);
        if (armadillos.isEmpty()) {
            return false;
        }
        for (ArmadilloEntity armadillo : armadillos) {
            if (armadillo.brushScute()) {
                return true;
            }
        }
        return false;
    }
}
