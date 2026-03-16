package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;

public record ApplySuspiciousStewEffectsFromItemAction(LootContext.EntityTarget entity) implements Action<ApplySuspiciousStewEffectsFromItemAction> {
    public static final MapCodec<ApplySuspiciousStewEffectsFromItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(ApplySuspiciousStewEffectsFromItemAction::entity)
    ).apply(instance, ApplySuspiciousStewEffectsFromItemAction::new));

    public static ApplySuspiciousStewEffectsFromItemAction of(LootContext.EntityTarget entity) {
        return new ApplySuspiciousStewEffectsFromItemAction(entity);
    }

    @Override
    public ActionType<ApplySuspiciousStewEffectsFromItemAction> type() {
        return ActionTypes.APPLY_SUSPICIOUS_STEW_EFFECTS_FROM_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        return false;
    }

    @Override
    public boolean execute(NewActionContext context) {
        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (ItemStackUtil.isNullOrEmpty(stack)) {
            return false;
        }

        SuspiciousStewEffectsComponent suspiciousStewEffects = stack.get(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS);
        if (suspiciousStewEffects == null) {
            return false;
        }

        Entity entity = context.get(this.entity.getParameter());
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        for (SuspiciousStewEffectsComponent.StewEffect effect : suspiciousStewEffects.effects()) {
            livingEntity.addStatusEffect(effect.createStatusEffectInstance());
        }

        return true;
    }
}
