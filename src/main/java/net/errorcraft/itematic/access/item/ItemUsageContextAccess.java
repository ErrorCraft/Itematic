package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.world.action.context.ItemStackExchanger;

public interface ItemUsageContextAccess {
    default ItemStackExchanger itematic$stackExchanger() {
        return null;
    }
}
