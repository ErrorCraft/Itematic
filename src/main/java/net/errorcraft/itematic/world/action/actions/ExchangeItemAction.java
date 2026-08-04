package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record ExchangeItemAction(ItemStack item, boolean decrementCount) implements Action<ExchangeItemAction> {
    public static final MapCodec<ExchangeItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemStack.CODEC.fieldOf("item").forGetter(ExchangeItemAction::item),
        Codec.BOOL.optionalFieldOf("decrement_count", true).forGetter(ExchangeItemAction::decrementCount)
    ).apply(instance, ExchangeItemAction::new));

    public static ExchangeItemAction of(Holder<Item> item) {
        return new ExchangeItemAction(new ItemStack(item), true);
    }

    public static ExchangeItemAction ofNoDecrement(Holder<Item> item) {
        return new ExchangeItemAction(new ItemStack(item), false);
    }

    public static ExchangeItemAction of(Holder<Item> item, DataComponentPatch components) {
        return new ExchangeItemAction(new ItemStack(item, 1, components), true);
    }

    @Override
    public ActionType<ExchangeItemAction> type() {
        return ActionTypes.EXCHANGE_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (this.decrementCount) {
            context.resultStack().shrink(1);
        }

        context.exchangeStack(this.item.copy());
        return true;
    }
}
