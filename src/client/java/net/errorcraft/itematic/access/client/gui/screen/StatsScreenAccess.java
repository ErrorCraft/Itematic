package net.errorcraft.itematic.access.client.gui.screen;

import net.minecraft.stat.StatHandler;

public interface StatsScreenAccess {
    default StatHandler itematic$statHandler() {
        return null;
    }
}
