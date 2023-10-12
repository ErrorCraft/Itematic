package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChestMinecartEntity.class)
public abstract class ChestMinecartEntityExtender extends VehicleEntityExtender {
    public ChestMinecartEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected RegistryKey<Item> asItemKey() {
        return ItemKeys.CHEST_MINECART;
    }
}
