package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;

public record ApplySuspiciousStewEffectsFromItemAction(ActionContextParameter entity) implements Action<ApplySuspiciousStewEffectsFromItemAction> {
    public static final MapCodec<ApplySuspiciousStewEffectsFromItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("entity").forGetter(ApplySuspiciousStewEffectsFromItemAction::entity)
    ).apply(instance, ApplySuspiciousStewEffectsFromItemAction::new));

    public static ApplySuspiciousStewEffectsFromItemAction of(ActionContextParameter entity) {
        return new ApplySuspiciousStewEffectsFromItemAction(entity);
    }

    @Override
    public ActionType<ApplySuspiciousStewEffectsFromItemAction> type() {
        return ActionTypes.APPLY_SUSPICIOUS_STEW_EFFECTS_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        return context.livingEntity(this.entity)
            .map(entity -> {
                SuspiciousStewEffectsComponent suspiciousStewEffects = context.stack().get(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS);
                if (suspiciousStewEffects == null) {
                    return false;
                }

                for (SuspiciousStewEffectsComponent.StewEffect effect : suspiciousStewEffects.effects()) {
                    entity.addStatusEffect(effect.createStatusEffectInstance());
                }
                return true;
            })
            .orElse(false);
    }
}
