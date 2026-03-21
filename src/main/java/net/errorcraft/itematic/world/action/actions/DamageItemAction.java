package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.dynamic.Codecs;

public record DamageItemAction(int amount) implements Action<DamageItemAction> {
    public static final MapCodec<DamageItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("amount").forGetter(DamageItemAction::amount)
    ).apply(instance, DamageItemAction::new));

    public static DamageItemAction of(int amount) {
        return new DamageItemAction(amount);
    }

    @Override
    public ActionType<DamageItemAction> type() {
        return ActionTypes.DAMAGE_ITEM;
    }

    @Override
    public boolean execute(NewActionContext context) {
        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        if (!stack.isDamageable()) {
            return false;
        }

        if (preventDamage(context)) {
            return false;
        }

        stack.itematic$damage(this.amount, context);
        return true;
    }

    private static boolean preventDamage(NewActionContext context) {
        return context.get(LootContextParameters.THIS_ENTITY) instanceof LivingEntity entity
            && entity.isInCreativeMode();
    }
}
