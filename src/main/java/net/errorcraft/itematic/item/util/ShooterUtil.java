package net.errorcraft.itematic.item.util;

import net.errorcraft.itematic.mixin.item.CrossbowItemAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;

import java.util.List;

public class ShooterUtil {
    private static DynamicRegistryManager registryManager;

    private ShooterUtil() {}

    public static DynamicRegistryManager registryManager() {
        return registryManager;
    }

    public static List<ItemStack> getLoadedAmmunition(ItemStack crossbow, DynamicRegistryManager registryManager) {
        ShooterUtil.registryManager = registryManager;
        List<ItemStack> ammunition = CrossbowItemAccessor.getProjectiles(crossbow);
        ShooterUtil.registryManager = null;
        return ammunition;
    }
}
