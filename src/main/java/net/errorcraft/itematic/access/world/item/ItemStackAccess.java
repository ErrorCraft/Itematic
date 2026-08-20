package net.errorcraft.itematic.access.world.item;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public interface ItemStackAccess {
    default ResourceKey<Item> itematic$key() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setComponents(PatchedDataComponentMap components) {}
    default void itematic$tryIncrement(int count) {}
    default int itematic$tryDecrement(int amount) {
        return 0;
    }
    default ItemStack itematic$transmuteCopy(Holder<Item> item) {
        return ItemStack.EMPTY;
    }
    default ItemStack itematic$transmuteCopy(Holder<Item> item, int count) {
        return ItemStack.EMPTY;
    }
    default ItemStack itematic$transmuteCopyIgnoreEmpty(Holder<Item> item, int count) {
        return ItemStack.EMPTY;
    }
    default boolean itematic$is(ResourceKey<Item> item) {
        return false;
    }
    default void itematic$damage(int amount, ActionContext context) {}
    default <T extends ItemBehavior<T>> boolean itematic$hasBehavior(ItemBehaviorType<T> type) {
        return false;
    }
    default <T extends ItemBehavior<T>> Optional<T> itematic$getBehavior(ItemBehaviorType<T> type) {
        return Optional.empty();
    }
    default boolean itematic$invokeEvent(ItemEvent event, ActionContext context) {
        return false;
    }
    default boolean itematic$hasEventListener(ItemEvent event) {
        return false;
    }
    default boolean itematic$mayStartUsing(Level level, Player user, InteractionHand hand, ItemStack stack) {
        return false;
    }
    default double itematic$attackSpeedMultiplier() {
        return 0.0d;
    }
}
