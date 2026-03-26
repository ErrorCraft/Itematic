package net.errorcraft.itematic.inventory;

import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;

public class SimpleStackReference implements StackReference {
    private ItemStack stack;

    private SimpleStackReference(ItemStack stack) {
        this.stack = stack;
    }

    public static SimpleStackReference of(ItemStack stack) {
        return new SimpleStackReference(stack);
    }

    @Override
    public ItemStack get() {
        return this.stack;
    }

    @Override
    public boolean set(ItemStack stack) {
        if (stack == null) {
            return false;
        }

        if (this.stack == stack) {
            return false;
        }

        this.stack = stack;
        return true;
    }
}
