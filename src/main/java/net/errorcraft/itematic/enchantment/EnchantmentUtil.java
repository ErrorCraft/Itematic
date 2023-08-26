package net.errorcraft.itematic.enchantment;

import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;

public class EnchantmentUtil {
    private EnchantmentUtil() {}

    public static ItemStack addEnchantment(ItemStack stack, EnchantmentLevelEntry entry) {
        EnchantedBookItem.addEnchantment(stack, entry);
        return stack;
    }
}
