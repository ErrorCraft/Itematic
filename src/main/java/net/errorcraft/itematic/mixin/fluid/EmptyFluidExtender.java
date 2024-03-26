package net.errorcraft.itematic.mixin.fluid;

import net.errorcraft.itematic.access.fluid.FluidAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.fluid.EmptyFluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EmptyFluid.class)
public class EmptyFluidExtender implements FluidAccess {
    @Override
    public RegistryKey<Item> itematic$getBucketItemKey() {
        return ItemKeys.AIR;
    }
}
