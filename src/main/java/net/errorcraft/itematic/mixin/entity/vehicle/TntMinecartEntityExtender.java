package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TntMinecartEntity.class)
public abstract class TntMinecartEntityExtender extends VehicleEntityExtender {
    public TntMinecartEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected RegistryKey<Item> asItemKey() {
        return ItemKeys.TNT_MINECART;
    }
}
