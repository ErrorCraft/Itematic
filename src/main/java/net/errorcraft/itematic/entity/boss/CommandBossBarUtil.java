package net.errorcraft.itematic.entity.boss;

import net.minecraft.registry.RegistryWrapper;

public class CommandBossBarUtil {
    private static RegistryWrapper.WrapperLookup lookup;

    private CommandBossBarUtil() {}

    public static RegistryWrapper.WrapperLookup lookup() {
        return lookup;
    }

    public static void setLookup(RegistryWrapper.WrapperLookup lookup) {
        CommandBossBarUtil.lookup = lookup;
    }
}
