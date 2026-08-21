package net.errorcraft.itematic.mixin.world.level.material;

import net.errorcraft.itematic.access.world.level.material.FluidAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.WaterFluid;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WaterFluid.class)
public class WaterFluidExtender implements FluidAccess {
    @Override
    public @Nullable ResourceKey<Item> itematic$getBucketItemId() {
        return ItemIds.WATER_BUCKET;
    }
}
