package net.errorcraft.itematic.mixin.fluid;

import net.errorcraft.itematic.access.fluid.FluidAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LavaFluid.class)
public class LavaFluidExtender implements FluidAccess {
    @Override
    public RegistryKey<Item> itematic$getBucketItemKey() {
        return ItemKeys.LAVA_BUCKET;
    }
}
