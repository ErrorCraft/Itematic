package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.access.item.ItemUsageContextAccess;
import net.minecraft.item.ItemUsageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemUsageContext.class)
public class ItemUsageContextExtender implements ItemUsageContextAccess {
    @Unique
    private boolean ignoresPlacementComponent;

    @Override
    public boolean itematic$ignoresPlacementComponent() {
        return this.ignoresPlacementComponent;
    }

    @Override
    public void itematic$setIgnoresPlacementComponent(boolean ignoresPlacementComponent) {
        this.ignoresPlacementComponent = ignoresPlacementComponent;
    }
}
