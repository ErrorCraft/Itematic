package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;

public record RemoveStatusEffectsAction(RegistryEntryList<StatusEffect> effects, ActionContextParameter entity) implements Action<RemoveStatusEffectsAction> {
    public static final MapCodec<RemoveStatusEffectsAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryCodecs.entryList(RegistryKeys.STATUS_EFFECT).fieldOf("effects").forGetter(RemoveStatusEffectsAction::effects),
        ActionContextParameter.CODEC.fieldOf("entity").forGetter(RemoveStatusEffectsAction::entity)
    ).apply(instance, RemoveStatusEffectsAction::new));

    @SafeVarargs
    public static RemoveStatusEffectsAction of(ActionContextParameter entity, RegistryEntry<StatusEffect>... effects) {
        return new RemoveStatusEffectsAction(RegistryEntryList.of(effects), entity);
    }

    @Override
    public ActionType<RemoveStatusEffectsAction> type() {
        return ActionTypes.REMOVE_STATUS_EFFECTS;
    }

    @Override
    public boolean execute(ActionContext context) {
        return context.livingEntity(this.entity)
            .map(this::removeStatusEffects)
            .orElse(false);
    }

    private boolean removeStatusEffects(LivingEntity target) {
        boolean removedStatusEffects = false;
        for (RegistryEntry<StatusEffect> effect : this.effects) {
            removedStatusEffects |= target.removeStatusEffect(effect);
        }
        return removedStatusEffects;
    }
}
