package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HopperMinecartEntity.class)
public abstract class HopperMinecartEntityExtender extends VehicleEntityExtender {
    public HopperMinecartEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(
        method = "getPickBlockStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForHopperMinecartUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.HOPPER_MINECART);
    }

    @Override
    protected RegistryKey<Item> asItemKey() {
        return ItemKeys.HOPPER_MINECART;
    }
}
