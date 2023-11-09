package net.errorcraft.itematic.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemStackConsumer {
    void set(ItemStack stack);
}
