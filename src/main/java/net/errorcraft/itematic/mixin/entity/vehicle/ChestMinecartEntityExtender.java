package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.access.entity.vehicle.AbstractMinecartEntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChestMinecartEntity.class)
public class ChestMinecartEntityExtender implements AbstractMinecartEntityAccess {
    @Override
    public RegistryKey<Item> asItemKey() {
        return ItemKeys.CHEST_MINECART;
    }
}
