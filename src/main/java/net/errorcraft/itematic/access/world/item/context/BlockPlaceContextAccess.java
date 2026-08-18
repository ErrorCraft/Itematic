package net.errorcraft.itematic.access.world.item.context;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.world.item.context.BlockPlaceContext;

public interface BlockPlaceContextAccess {
    default BlockPlaceContext itematic$offset(int x, int y, int z) {
        throw new AssertionError("Implemented via mixin");
    }
    default ActionContext itematic$actionContext(ItemStackExchanger stackExchanger) {
        throw new AssertionError("Implemented via mixin");
    }
}
