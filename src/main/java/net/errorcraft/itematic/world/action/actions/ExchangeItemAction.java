package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemUsageUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

public record ExchangeItemAction(RegistryEntry<Item> item, ComponentChanges components, boolean decrementCount) implements Action<ExchangeItemAction> {
    public static final MapCodec<ExchangeItemAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(ExchangeItemAction::item),
        ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter(ExchangeItemAction::components),
        Codec.BOOL.optionalFieldOf("decrement_count", true).forGetter(ExchangeItemAction::decrementCount)
    ).apply(instance, ExchangeItemAction::new));

    public static ExchangeItemAction of(RegistryEntry<Item> item) {
        return new ExchangeItemAction(item, ComponentChanges.EMPTY, true);
    }

    public static ExchangeItemAction ofNoDecrement(RegistryEntry<Item> item) {
        return new ExchangeItemAction(item, ComponentChanges.EMPTY, false);
    }

    public static ExchangeItemAction of(RegistryEntry<Item> item, ComponentChanges components) {
        return new ExchangeItemAction(item, components, true);
    }

    @Override
    public ActionType<ExchangeItemAction> type() {
        return ActionTypes.EXCHANGE_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        ItemStack resultStack = this.exchange(new ItemStack(this.item, 1, this.components), context);
        context.setResultStack(resultStack);
        return true;
    }

    public ItemStack exchange(ItemStack resultStack, ActionContext context) {
        return context.player(ActionContextParameter.THIS)
            .map(player -> ItemUsageUtil.exchangeStack(context.stack(), player, resultStack, true, this.decrementCount))
            .orElseGet(() -> {
                ItemStack stack = context.stack();
                if (this.decrementCount) {
                    stack.decrement(1);
                }
                if (stack.isEmpty()) {
                    return resultStack;
                }
                ItemDispenserBehavior.spawnItem(context.world(), resultStack, 6, context.side(), context.position(ActionContextParameter.TARGET));
                return stack;
            });
    }
}
