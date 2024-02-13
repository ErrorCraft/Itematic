package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;

public record DecrementItemAction(int amount, boolean ignoreGameMode) implements Action<DecrementItemAction> {
    public static final Codec<DecrementItemAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("amount").forGetter(DecrementItemAction::amount),
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "ignore_game_mode", false).forGetter(DecrementItemAction::ignoreGameMode)
    ).apply(instance, DecrementItemAction::new));

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

    public static DecrementItemAction of(int amount) {
        return new DecrementItemAction(amount, false);
    }
}
