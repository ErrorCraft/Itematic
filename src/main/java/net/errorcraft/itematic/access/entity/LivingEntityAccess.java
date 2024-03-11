package net.errorcraft.itematic.access.entity;

import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface LivingEntityAccess {
    default boolean itematic$isHolding(RegistryKey<Item> key) {
        return false;
    }
    default ItemStack itematic$getAmmunition(ShooterItemComponent itemComponent) {
        return ItemStack.EMPTY;
    }
    default void itematic$eatFood(World world, ItemStack stack, ItemStackConsumer resultStackConsumer) {}
    default void itematic$startUsingHand(Hand hand, int ticks) {}
    default int itematic$itemUsedTicks() {
        return 0;
    }
}
