package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;

public record ClearStatusEffectsAction(ActionContextParameter entity) implements Action<ClearStatusEffectsAction> {
    public static final MapCodec<ClearStatusEffectsAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("entity").forGetter(ClearStatusEffectsAction::entity)
    ).apply(instance, ClearStatusEffectsAction::new));

    public static ClearStatusEffectsAction of(ActionContextParameter entity) {
        return new ClearStatusEffectsAction(entity);
    }

    @Override
    public ActionType<ClearStatusEffectsAction> type() {
        return ActionTypes.CLEAR_STATUS_EFFECTS;
    }

    @Override
    public boolean execute(ActionContext context) {
        Optional<Entity> optionalEntity = context.entity(this.entity);
        if (optionalEntity.isEmpty()) {
            return false;
        }
        if (optionalEntity.get() instanceof LivingEntity target) {
            target.clearStatusEffects();
            return true;
        }
        return false;
    }
}
