package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public interface ItemStackAccess {
    default RegistryKey<Item> itematic$key() {
        return null;
    }
    default void itematic$setComponents(ComponentMapImpl components) {}
    default void itematic$tryIncrement(int count) {}
    default int itematic$tryDecrement(int amount) {
        return 0;
    }
    default ItemStack itematic$copyWithItem(RegistryEntry<Item> item) {
        return ItemStack.EMPTY;
    }
    default ItemStack itematic$copyComponentsToNewStack(RegistryEntry<Item> item, int count) {
        return ItemStack.EMPTY;
    }
    default ItemStack itematic$copyComponentsToNewStackIgnoreEmpty(RegistryEntry<Item> item, int count) {
        return ItemStack.EMPTY;
    }
    default boolean itematic$isOf(RegistryKey<Item> key) {
        return false;
    }
    default void itematic$damage(int amount, ActionContext context) {}
    default <T extends ItemComponent<T>> boolean itematic$hasComponent(ItemComponentType<T> type) {
        return false;
    }
    default <T extends ItemComponent<T>> Optional<T> itematic$getComponent(ItemComponentType<T> type) {
        return Optional.empty();
    }
    default boolean itematic$invokeEvent(ItemEvent event, ActionContext context) {
        return false;
    }
    default boolean itematic$canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }
    default boolean itematic$isNetworkSynced() {
        return false;
    }
    default boolean itematic$mayStartUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        return false;
    }
    default double itematic$attackSpeedMultiplier() {
        return 0.0d;
    }
}
