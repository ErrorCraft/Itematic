package net.errorcraft.itematic.access.block.entity;

import net.minecraft.item.ItemStack;

public interface BannerBlockEntityAccess {
    default ItemStack itematic$getPickStack(ItemStack stack) {
        return stack;
    }
}
