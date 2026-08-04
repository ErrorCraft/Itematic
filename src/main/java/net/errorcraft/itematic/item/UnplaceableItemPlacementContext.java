package net.errorcraft.itematic.item;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;

public class UnplaceableItemPlacementContext extends BlockPlaceContext {
    private UnplaceableItemPlacementContext(UseOnContext context) {
        super(context);
    }

    @Override
    public boolean canPlace() {
        return false;
    }

    public static UnplaceableItemPlacementContext of(UseOnContext context) {
        return new UnplaceableItemPlacementContext(context);
    }
}
