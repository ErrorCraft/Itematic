package net.errorcraft.itematic.access.world.item.context;

import net.errorcraft.itematic.world.action.context.ItemStackExchanger;

public interface UseOnContextAccess {
    default ItemStackExchanger itematic$stackExchanger() {
        throw new AssertionError("Implemented via mixin");
    }
}
