package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;

public record RemoveStatusEffectsAction(HolderSet<MobEffect> effects, LootContext.EntityTarget entity) implements Action<RemoveStatusEffectsAction> {
    public static final MapCodec<RemoveStatusEffectsAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryCodecs.homogeneousList(Registries.MOB_EFFECT).fieldOf("effects").forGetter(RemoveStatusEffectsAction::effects),
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(RemoveStatusEffectsAction::entity)
    ).apply(instance, RemoveStatusEffectsAction::new));

    @SafeVarargs
    public static RemoveStatusEffectsAction of(LootContext.EntityTarget entity, Holder<MobEffect>... effects) {
        return new RemoveStatusEffectsAction(HolderSet.direct(effects), entity);
    }

    @Override
    public ActionType<RemoveStatusEffectsAction> type() {
        return ActionTypes.REMOVE_STATUS_EFFECTS;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (context.get(this.entity.contextParam()) instanceof LivingEntity target) {
            return this.removeStatusEffects(target);
        }

        return false;
    }

    private boolean removeStatusEffects(LivingEntity target) {
        boolean removedStatusEffects = false;
        for (Holder<MobEffect> effect : this.effects) {
            removedStatusEffects |= target.removeEffect(effect);
        }

        return removedStatusEffects;
    }
}
