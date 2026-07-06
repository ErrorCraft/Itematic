package net.errorcraft.itematic.access.client;

import net.errorcraft.itematic.client.item.bar.ItemBarStyleLoader;

public interface MinecraftClientAccess {
    default ItemBarStyleLoader itematic$itemBarStyles() {
        return null;
    }
}
