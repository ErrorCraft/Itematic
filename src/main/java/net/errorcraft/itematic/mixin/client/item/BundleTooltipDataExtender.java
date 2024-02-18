package net.errorcraft.itematic.mixin.client.item;

import net.errorcraft.itematic.access.client.item.BundleTooltipDataAccess;
import net.minecraft.client.item.BundleTooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BundleTooltipData.class)
public class BundleTooltipDataExtender implements BundleTooltipDataAccess {
    @Unique
    private int capacity;

    @Override
    public int itematic$capacity() {
        return this.capacity;
    }

    @Override
    public void itematic$setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
