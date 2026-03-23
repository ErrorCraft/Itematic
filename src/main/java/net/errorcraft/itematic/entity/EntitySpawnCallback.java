package net.errorcraft.itematic.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface EntitySpawnCallback<T extends Entity> {
    void accept(T entity, ItemStack stack);
}
