package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.Hand;

import java.util.Optional;

public record ExchangeItemAction(RegistryEntry<Item> item) implements Action {
    public static final Codec<ExchangeItemAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(ExchangeItemAction::item)
    ).apply(instance, ExchangeItemAction::new));

    @Override
    public ActionType<?> type() {
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
        Optional<Hand> hand = context.hand();
        if (hand.isEmpty()) {
            return false;
        }
        ItemStack stack = ItemUsage.exchangeStack(context.stack(), player, new ItemStack(this.item));
        player.setStackInHand(hand.get(), stack);
        return true;
    }
}
