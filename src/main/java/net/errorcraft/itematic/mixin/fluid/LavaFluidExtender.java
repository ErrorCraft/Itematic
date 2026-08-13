package net.errorcraft.itematic.mixin.fluid;

import net.errorcraft.itematic.access.fluid.FluidAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LavaFluid.class)
public class LavaFluidExtender implements FluidAccess {
    @Override
    public ResourceKey<Item> itematic$getBucketItemKey() {
        return ItemIds.LAVA_BUCKET;
    }
}
