package net.errorcraft.itematic.world.item;

import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;

public class ItemStacks {
    private ItemStacks() {}

    public static boolean isNullOrEmpty(@Nullable ItemStack stack) {
        return stack == null || stack.isEmpty();
    }

    public static ItemStack fromItemInstance(ItemInstance item) {
        return switch (item) {
            case ItemStack itemStack -> itemStack;
            case ItemStackTemplate itemStackTemplate -> itemStackTemplate.create();
            default -> ItemStack.EMPTY;
        };
    }
}
