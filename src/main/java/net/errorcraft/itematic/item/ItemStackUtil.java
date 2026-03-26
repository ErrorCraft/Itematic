package net.errorcraft.itematic.item;

import net.minecraft.item.ItemStack;

public class ItemStackUtil {
    private ItemStackUtil() {}

    public static boolean isNullOrEmpty(ItemStack stack) {
        return stack == null || stack.isEmpty();
    }
}
