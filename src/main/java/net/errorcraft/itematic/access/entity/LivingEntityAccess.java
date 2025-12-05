package net.errorcraft.itematic.access.entity;

import net.errorcraft.itematic.item.ItemStackConsumer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface LivingEntityAccess {
    default boolean itematic$isHolding(RegistryKey<Item> key) {
        return false;
    }
    default ItemStack itematic$getAmmunition(ItemStack stack) {
        return ItemStack.EMPTY;
    }
    default void itematic$eatFood(World world, ItemStack stack, ItemStackConsumer resultStackConsumer) {}
    default void itematic$startUsingHand(Hand hand, int ticks) {}
    default int itematic$itemUsedTicks() {
        return 0;
    }
    default double itematic$getAttackDamage() {
        return 0.0d;
    }
}
