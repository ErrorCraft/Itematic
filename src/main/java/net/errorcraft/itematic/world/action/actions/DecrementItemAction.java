package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.item.ItemStack;

public record DecrementItemAction(int amount, boolean ignoreGameMode) implements Action<DecrementItemAction> {
    public static final MapCodec<DecrementItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("amount").forGetter(DecrementItemAction::amount),
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
        ItemStack stack = context.stack();
        if (stack.isEmpty()) {
            return false;
        }
        if (this.ignoreGameMode) {
            stack.decrement(this.amount);
        } else {
            stack.decrementUnlessCreative(this.amount, context.livingEntity(ActionContextParameter.THIS).orElse(null));
        }
        return true;
    }
}
