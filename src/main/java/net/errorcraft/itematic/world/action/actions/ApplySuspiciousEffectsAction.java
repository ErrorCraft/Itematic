package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.item.SuspiciousStewItemAccessor;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;

public record ApplySuspiciousEffectsAction(ActionContextParameter entity) implements Action<ApplySuspiciousEffectsAction> {
    public static final Codec<ApplySuspiciousEffectsAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("entity").forGetter(ApplySuspiciousEffectsAction::entity)
    ).apply(instance, ApplySuspiciousEffectsAction::new));

    @Override
    public ActionType<ApplySuspiciousEffectsAction> type() {
        return ActionTypes.APPLY_SUSPICIOUS_EFFECTS;
    }

    @Override
    public boolean execute(ActionContext context) {
        return context.livingEntity(this.entity)
            .map(entity -> {
                SuspiciousStewItemAccessor.forEachEffect(context.stack(), effect -> entity.addStatusEffect(effect.createStatusEffectInstance()));
                return true;
            })
            .orElse(false);
    }

    public static ApplySuspiciousEffectsAction of(ActionContextParameter entity) {
        return new ApplySuspiciousEffectsAction(entity);
    }
}
