package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.access.item.ItemUsageContextAccess;
import net.minecraft.item.ItemUsageContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemUsageContext.class)
public class ItemUsageContextExtender implements ItemUsageContextAccess {
    private boolean ignoresPlacementComponent;

    @Override
    public boolean ignoresPlacementComponent() {
        return this.ignoresPlacementComponent;
    }

    @Override
    public void setIgnoresPlacementComponent(boolean ignoresPlacementComponent) {
        this.ignoresPlacementComponent = ignoresPlacementComponent;
    }
}
