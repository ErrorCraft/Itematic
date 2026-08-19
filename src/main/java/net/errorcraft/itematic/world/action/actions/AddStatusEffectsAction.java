package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.List;

public record AddStatusEffectsAction(List<MobEffectInstance> effects, LootContext.EntityTarget entity) implements Action<AddStatusEffectsAction> {
    public static final MapCodec<AddStatusEffectsAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        MobEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(AddStatusEffectsAction::effects),
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(AddStatusEffectsAction::entity)
    ).apply(instance, AddStatusEffectsAction::new));

    public static AddStatusEffectsAction of(MobEffectInstance... effects) {
        return new AddStatusEffectsAction(List.of(effects), LootContext.EntityTarget.THIS);
    }

    @Override
    public ActionType<AddStatusEffectsAction> type() {
        return ActionType.ADD_STATUS_EFFECTS;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (context.get(this.entity.contextParam()) instanceof LivingEntity target) {
            return this.addStatusEffects(target);
        }

        return false;
    }

    private boolean addStatusEffects(LivingEntity target) {
        boolean addedStatusEffects = false;
        for (MobEffectInstance effect : this.effects) {
            addedStatusEffects |= target.addEffect(effect);
        }

        return addedStatusEffects;
    }
}
