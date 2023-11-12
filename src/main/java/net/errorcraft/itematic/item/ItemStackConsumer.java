package net.errorcraft.itematic.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemStackConsumer {
    ItemStackConsumer EMPTY = stack -> {};

    void set(ItemStack stack);
}
