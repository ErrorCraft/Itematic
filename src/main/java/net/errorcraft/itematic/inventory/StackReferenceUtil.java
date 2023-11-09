package net.errorcraft.itematic.inventory;

import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;

public class StackReferenceUtil {
    private StackReferenceUtil() {}

    public static StackReference of(ItemStack stack) {
        return new Simple(stack);
    }

    private static class Simple implements StackReference {
        private ItemStack stack;

        private Simple(ItemStack stack) {
            this.stack = stack;
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
}
