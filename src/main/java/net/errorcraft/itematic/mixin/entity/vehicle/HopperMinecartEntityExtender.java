package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HopperMinecartEntity.class)
public abstract class HopperMinecartEntityExtender extends VehicleEntityExtender {
    public HopperMinecartEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected RegistryKey<Item> asItemKey() {
        return ItemKeys.HOPPER_MINECART;
    }
}
