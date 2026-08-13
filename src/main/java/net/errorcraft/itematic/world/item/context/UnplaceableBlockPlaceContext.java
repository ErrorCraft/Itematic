package net.errorcraft.itematic.world.item.context;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;

public class UnplaceableBlockPlaceContext extends BlockPlaceContext {
    private UnplaceableBlockPlaceContext(UseOnContext context) {
        super(context);
    }

    public static UnplaceableBlockPlaceContext of(UseOnContext context) {
        return new UnplaceableBlockPlaceContext(context);
    }

    @Override
    public boolean canPlace() {
        return false;
    }
}
