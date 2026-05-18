package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;

public interface ItemPlacementContextAccess {
    default ItemPlacementContext itematic$offset(int x, int y, int z) {
        return null;
    }
    default ActionContext itematic$actionContext(ServerWorld world, ItemStackExchanger stackExchanger) {
        return null;
    }
}
