package net.errorcraft.itematic.item;

import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;

public class UnplaceableItemPlacementContext extends ItemPlacementContext {
    private UnplaceableItemPlacementContext(ItemUsageContext context) {
        super(context);
    }

    @Override
    public boolean canPlace() {
        return false;
    }

    public static UnplaceableItemPlacementContext of(ItemUsageContext context) {
        return new UnplaceableItemPlacementContext(context);
    }
}
