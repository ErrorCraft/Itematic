package net.errorcraft.itematic.access.item;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.item.ItemPlacementContext;

public interface ItemPlacementContextAccess {
    default ItemPlacementContext itematic$offset(int x, int y, int z) {
        return null;
    }
    default ActionContext itematic$actionContext(ItemStackExchanger stackExchanger) {
        return null;
    }
}
