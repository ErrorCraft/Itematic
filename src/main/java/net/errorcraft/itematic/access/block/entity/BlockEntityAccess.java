package net.errorcraft.itematic.access.block.entity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;

public interface BlockEntityAccess {
    default void readNbt(NbtCompound nbt, DynamicRegistryManager registryManager) {}
    default DynamicRegistryManager getRegistryManager() {
        return null;
    }
}
