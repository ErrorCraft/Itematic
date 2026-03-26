package net.errorcraft.itematic.access.entity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Hand;

public interface LivingEntityAccess {
    default boolean itematic$hasStackInInventory(ItemStack stack) {
        return false;
    }
    default boolean itematic$isHolding(RegistryKey<Item> key) {
        return false;
    }
    default ItemStack itematic$getAmmunition(ItemStack stack) {
        return ItemStack.EMPTY;
    }
    default void itematic$startUsingHand(Hand hand, int ticks) {}
    default int itematic$itemUsedTicks() {
        return 0;
    }
    default double itematic$getAttackDamage() {
        return 0.0d;
    }
}
