package net.errorcraft.itematic.access.world.entity;

import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface LivingEntityAccess {
    default boolean itematic$hasStackInInventory(ItemStack stack) {
        return false;
    }
    default boolean itematic$isHolding(ResourceKey<Item> item) {
        return false;
    }
    default ItemStack itematic$getHeldItem(HolderSet<Item> items) {
        return ItemStack.EMPTY;
    }
    default ItemStack itematic$getAmmunition(ItemStack stack) {
        return ItemStack.EMPTY;
    }
    default void itematic$startUsingItem(InteractionHand hand, int ticks) {}
    default int itematic$usedItemTicks() {
        return 0;
    }
    default double itematic$getAttackDamage() {
        return 0.0d;
    }
    default double itematic$getBaseAttackDamage() {
        return 0.0d;
    }
}
