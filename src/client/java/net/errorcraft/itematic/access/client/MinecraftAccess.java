package net.errorcraft.itematic.access.client;

import net.errorcraft.itematic.client.resources.item.bar.ItemBarStyleManager;

public interface MinecraftAccess {
    default ItemBarStyleManager itematic$itemBarStyles() {
        throw new AssertionError("Implemented via mixin");
    }
}
