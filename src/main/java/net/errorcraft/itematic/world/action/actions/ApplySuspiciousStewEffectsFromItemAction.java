package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record ApplySuspiciousStewEffectsFromItemAction(LootContext.EntityTarget entity) implements Action<ApplySuspiciousStewEffectsFromItemAction> {
    public static final MapCodec<ApplySuspiciousStewEffectsFromItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(ApplySuspiciousStewEffectsFromItemAction::entity)
    ).apply(instance, ApplySuspiciousStewEffectsFromItemAction::new));

    public static ApplySuspiciousStewEffectsFromItemAction of(LootContext.EntityTarget entity) {
        return new ApplySuspiciousStewEffectsFromItemAction(entity);
    }

    @Override
    public ActionType<ApplySuspiciousStewEffectsFromItemAction> type() {
        return ActionType.APPLY_SUSPICIOUS_STEW_EFFECTS_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = context.get(LootContextParams.TOOL);
        if (ItemStacks.isNullOrEmpty(stack)) {
            return false;
        }

        SuspiciousStewEffects suspiciousStewEffects = stack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
        if (suspiciousStewEffects == null) {
            return false;
        }

        Entity entity = context.get(this.entity.contextParam());
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        for (SuspiciousStewEffects.Entry effect : suspiciousStewEffects.effects()) {
            livingEntity.addEffect(effect.createEffectInstance());
        }

        return true;
    }
}
