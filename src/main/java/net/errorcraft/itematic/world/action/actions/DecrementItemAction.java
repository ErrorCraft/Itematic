package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.dynamic.Codecs;

public record DecrementItemAction(int amount, boolean ignoreGameMode) implements Action<DecrementItemAction> {
    public static final MapCodec<DecrementItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("amount").forGetter(DecrementItemAction::amount),
        Codec.BOOL.optionalFieldOf("ignore_game_mode", false).forGetter(DecrementItemAction::ignoreGameMode)
    ).apply(instance, DecrementItemAction::new));

    public static DecrementItemAction of(int amount) {
        return new DecrementItemAction(amount, false);
    }

    @Override
    public ActionType<DecrementItemAction> type() {
        return ActionTypes.DECREMENT_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        if (this.ignoreGameMode) {
            stack.decrement(this.amount);
        } else {
            Entity entity = context.get(LootContextParameters.THIS_ENTITY);
            stack.decrementUnlessCreative(
                this.amount,
                entity instanceof LivingEntity livingEntity ? livingEntity : null
            );
        }

        return true;
    }
}
