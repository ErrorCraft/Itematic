package net.errorcraft.itematic.mixin.fluid;

import net.errorcraft.itematic.access.fluid.FluidAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.EmptyFluid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EmptyFluid.class)
public class EmptyFluidExtender implements FluidAccess {
    @Override
    public ResourceKey<Item> itematic$getBucketItemKey() {
        return ItemIds.AIR;
    }
}
