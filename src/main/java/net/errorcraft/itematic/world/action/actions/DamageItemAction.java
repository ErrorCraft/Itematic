package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record DamageItemAction(int amount) implements Action<DamageItemAction> {
    public static final MapCodec<DamageItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(DamageItemAction::amount)
    ).apply(instance, DamageItemAction::new));

    public static DamageItemAction of(int amount) {
        return new DamageItemAction(amount);
    }

    @Override
    public ActionType<DamageItemAction> type() {
        return ActionType.DAMAGE_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = context.get(LootContextParams.TOOL);
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        if (!stack.isDamageableItem()) {
            return false;
        }

        if (preventDamage(context)) {
            return false;
        }

        stack.itematic$damage(this.amount, context);
        return true;
    }

    private static boolean preventDamage(ActionContext context) {
        return context.get(LootContextParams.THIS_ENTITY) instanceof LivingEntity entity
            && entity.hasInfiniteMaterials();
    }
}
