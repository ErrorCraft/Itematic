package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public record ExchangeItemAction(ItemStack stack, boolean decrementCount) implements Action<ExchangeItemAction> {
    public static final MapCodec<ExchangeItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemStack.CODEC.fieldOf("item").forGetter(ExchangeItemAction::stack),
        Codec.BOOL.optionalFieldOf("decrement_count", true).forGetter(ExchangeItemAction::decrementCount)
    ).apply(instance, ExchangeItemAction::new));

    public static ExchangeItemAction of(RegistryEntry<Item> item) {
        return new ExchangeItemAction(new ItemStack(item), true);
    }

    public static ExchangeItemAction ofNoDecrement(RegistryEntry<Item> item) {
        return new ExchangeItemAction(new ItemStack(item), false);
    }

    public static ExchangeItemAction of(RegistryEntry<Item> item, ComponentChanges components) {
        return new ExchangeItemAction(new ItemStack(item, 1, components), true);
    }

    @Override
    public ActionType<ExchangeItemAction> type() {
        return ActionTypes.EXCHANGE_ITEM;
    }

    @Override
    public boolean execute(NewActionContext context) {
        if (this.decrementCount) {
            context.resultStack().decrement(1);
        }

        context.exchangeStack(this.stack.copy());
        return true;
    }
}
