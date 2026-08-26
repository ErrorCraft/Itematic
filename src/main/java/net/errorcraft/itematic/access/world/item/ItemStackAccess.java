package net.errorcraft.itematic.access.world.item;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ItemStackAccess {
    default ResourceKey<Item> itematic$key() {
        throw new AssertionError("Implemented via mixin");
    }
    default int itematic$tryDecrement(int amount) {
        return 0;
    }
    default ItemStack itematic$transmuteCopy(Holder<Item> item) {
        return ItemStack.EMPTY;
    }
    default ItemStack itematic$transmuteCopy(Holder<Item> item, int count) {
        return ItemStack.EMPTY;
    }
    default void itematic$damage(int amount, ActionContext context) {}
    default boolean itematic$mayStartUsing(Level level, Player user, InteractionHand hand, ItemStack stack) {
        return false;
    }
    default double itematic$attackSpeedMultiplier() {
        return 0.0d;
    }
}
