package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemStackTemplates;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;

public record ExchangeItemAction(ItemStackTemplate item, boolean decrementCount) implements Action<ExchangeItemAction> {
    public static final MapCodec<ExchangeItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemStackTemplate.CODEC.fieldOf("item").forGetter(ExchangeItemAction::item),
        Codec.BOOL.optionalFieldOf("decrement_count", true).forGetter(ExchangeItemAction::decrementCount)
    ).apply(instance, ExchangeItemAction::new));

    public static ExchangeItemAction of(Holder<Item> item) {
        return new ExchangeItemAction(ItemStackTemplates.of(item), true);
    }

    public static ExchangeItemAction ofNoDecrement(Holder<Item> item) {
        return new ExchangeItemAction(ItemStackTemplates.of(item), false);
    }

    public static ExchangeItemAction of(Holder<Item> item, DataComponentPatch components) {
        return new ExchangeItemAction(ItemStackTemplates.of(item, components), true);
    }

    @Override
    public ActionType<ExchangeItemAction> type() {
        return ActionType.EXCHANGE_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        if (this.decrementCount) {
            context.resultStack().shrink(1);
        }

        context.exchangeStack(this.item.create());
        return true;
    }
}
