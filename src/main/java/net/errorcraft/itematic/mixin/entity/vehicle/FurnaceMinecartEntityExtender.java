package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FurnaceMinecartEntity.class)
public abstract class FurnaceMinecartEntityExtender extends VehicleEntityExtender {
    public FurnaceMinecartEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected RegistryKey<Item> asItemKey() {
        return ItemKeys.FURNACE_MINECART;
    }
}
