package net.errorcraft.itematic.world.item;

import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class ItemStacks {
    private ItemStacks() {}

    public static boolean isNullOrEmpty(@Nullable ItemStack stack) {
        return stack == null || stack.isEmpty();
    }
}
