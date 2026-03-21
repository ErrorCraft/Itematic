package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.loot.context.LootContext;

import java.util.List;

public record AddStatusEffectsAction(List<StatusEffectInstance> effects, LootContext.EntityTarget entity) implements Action<AddStatusEffectsAction> {
    public static final MapCodec<AddStatusEffectsAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        StatusEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(AddStatusEffectsAction::effects),
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(AddStatusEffectsAction::entity)
    ).apply(instance, AddStatusEffectsAction::new));

    public static AddStatusEffectsAction of(StatusEffectInstance... effects) {
        return new AddStatusEffectsAction(List.of(effects), LootContext.EntityTarget.THIS);
    }

    @Override
    public ActionType<AddStatusEffectsAction> type() {
        return ActionTypes.ADD_STATUS_EFFECTS;
    }

    @Override
    public boolean execute(NewActionContext context) {
        if (context.get(this.entity.getParameter()) instanceof LivingEntity target) {
            return this.addStatusEffects(target);
        }

        return false;
    }

    private boolean addStatusEffects(LivingEntity target) {
        boolean addedStatusEffects = false;
        for (StatusEffectInstance effect : this.effects) {
            addedStatusEffects |= target.addStatusEffect(effect);
        }

        return addedStatusEffects;
    }
}
