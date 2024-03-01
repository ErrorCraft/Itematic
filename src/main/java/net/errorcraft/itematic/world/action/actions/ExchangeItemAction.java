package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemUsageUtil;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

import java.util.Optional;

public record ExchangeItemAction(RegistryEntry<Item> item, boolean decrementCount) implements Action<ExchangeItemAction> {
    public static final Codec<ExchangeItemAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(ExchangeItemAction::item),
        Codec.BOOL.optionalFieldOf("decrement_count", true).forGetter(ExchangeItemAction::decrementCount)
    ).apply(instance, ExchangeItemAction::new));

    @Override
    public ActionType<ExchangeItemAction> type() {
        return ActionTypes.EXCHANGE_ITEM;
    }

    @Override
    public boolean execute(ActionContext context) {
        Optional<Entity> entity = context.entity(ActionContextParameter.THIS);
        if (entity.isEmpty()) {
            return false;
        }
        if (!(entity.get() instanceof PlayerEntity player)) {
            return false;
        }
        ItemStack stack = ItemUsageUtil.exchangeStack(context.stack(), player, new ItemStack(this.item), true, this.decrementCount);
        context.setResultStack(stack);
        return true;
    }

    public static ExchangeItemAction of(RegistryEntry<Item> item, boolean decrementCount) {
        return new ExchangeItemAction(item, decrementCount);
    }
}
