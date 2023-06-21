package net.errorcraft.itematic.mixin.fluid;

import net.errorcraft.itematic.access.fluid.FluidAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WaterFluid.class)
public class WaterFluidExtender implements FluidAccess {
    @Override
    public RegistryKey<Item> getBucketItemKey() {
        return ItemKeys.WATER_BUCKET;
    }
}
