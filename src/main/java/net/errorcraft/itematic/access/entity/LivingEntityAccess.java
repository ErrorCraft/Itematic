package net.errorcraft.itematic.access.entity;

import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Hand;

public interface LivingEntityAccess {
    default boolean isHolding(RegistryKey<Item> key) {
        return false;
    }
    default ItemStack getAmmunition(ShooterItemComponent itemComponent) {
        return ItemStack.EMPTY;
    }
    default Hand getHoldingHand(ItemStack stack) {
        return null;
    }
}
