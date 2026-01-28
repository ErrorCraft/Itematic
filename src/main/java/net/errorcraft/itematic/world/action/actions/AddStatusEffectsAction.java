package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.List;

public record AddStatusEffectsAction(List<StatusEffectInstance> effects, ActionContextParameter entity) implements Action<AddStatusEffectsAction> {
    public static final MapCodec<AddStatusEffectsAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        StatusEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(AddStatusEffectsAction::effects),
        ActionContextParameter.CODEC.fieldOf("entity").forGetter(AddStatusEffectsAction::entity)
    ).apply(instance, AddStatusEffectsAction::new));

    public static AddStatusEffectsAction of(StatusEffectInstance... effects) {
        return new AddStatusEffectsAction(List.of(effects), ActionContextParameter.THIS);
    }

    @Override
    public ActionType<AddStatusEffectsAction> type() {
        return ActionTypes.ADD_STATUS_EFFECTS;
    }

    @Override
    public boolean execute(ActionContext context) {
        return context.livingEntity(this.entity)
            .map(this::addStatusEffects)
            .orElse(false);
    }

    private boolean addStatusEffects(LivingEntity target) {
        boolean addedStatusEffects = false;
        for (StatusEffectInstance effect : this.effects) {
            addedStatusEffects |= target.addStatusEffect(effect);
        }

        return addedStatusEffects;
    }
}
