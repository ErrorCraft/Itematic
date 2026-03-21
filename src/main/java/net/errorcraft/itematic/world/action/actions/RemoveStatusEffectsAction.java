package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;

public record RemoveStatusEffectsAction(RegistryEntryList<StatusEffect> effects, LootContext.EntityTarget entity) implements Action<RemoveStatusEffectsAction> {
    public static final MapCodec<RemoveStatusEffectsAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryCodecs.entryList(RegistryKeys.STATUS_EFFECT).fieldOf("effects").forGetter(RemoveStatusEffectsAction::effects),
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(RemoveStatusEffectsAction::entity)
    ).apply(instance, RemoveStatusEffectsAction::new));

    @SafeVarargs
    public static RemoveStatusEffectsAction of(LootContext.EntityTarget entity, RegistryEntry<StatusEffect>... effects) {
        return new RemoveStatusEffectsAction(RegistryEntryList.of(effects), entity);
    }

    @Override
    public ActionType<RemoveStatusEffectsAction> type() {
        return ActionTypes.REMOVE_STATUS_EFFECTS;
    }

    @Override
    public boolean execute(NewActionContext context) {
        if (context.get(this.entity.getParameter()) instanceof LivingEntity target) {
            return this.removeStatusEffects(target);
        }

        return false;
    }

    private boolean removeStatusEffects(LivingEntity target) {
        boolean removedStatusEffects = false;
        for (RegistryEntry<StatusEffect> effect : this.effects) {
            removedStatusEffects |= target.removeStatusEffect(effect);
        }

        return removedStatusEffects;
    }
}
