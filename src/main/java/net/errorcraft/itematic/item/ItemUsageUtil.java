package net.errorcraft.itematic.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;

public class ItemUsageUtil {
    private static boolean decrementCount = true;

    private ItemUsageUtil() {}

    public static boolean decrementCount() {
        return ItemUsageUtil.decrementCount;
    }

    public static ItemStack exchangeStack(ItemStack inputStack, PlayerEntity player, ItemStack outputStack, boolean creativeOverride, boolean decrementCount) {
        ItemUsageUtil.decrementCount = decrementCount;
        ItemStack resultStack = ItemUsage.exchangeStack(inputStack, player, outputStack, creativeOverride);
        ItemUsageUtil.decrementCount = true;
        return resultStack;
    }
}
