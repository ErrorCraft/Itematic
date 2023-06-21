package net.errorcraft.itematic.access.fluid;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

public interface FluidAccess {
    default RegistryKey<Item> getBucketItemKey() {
        return null;
    }
}
