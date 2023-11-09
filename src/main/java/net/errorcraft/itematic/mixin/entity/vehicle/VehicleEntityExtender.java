package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VehicleEntity.class)
public abstract class VehicleEntityExtender extends Entity {
    public VehicleEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(
        method = "killAndDropItem",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack dropItemsNewItemStackUseRegistryEntry(ItemConvertible item) {
        return this.getWorld().itematic$createStack(this.asItemKey());
    }

    protected RegistryKey<Item> asItemKey() {
        return ItemKeys.MINECART;
    }
}
