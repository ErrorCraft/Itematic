package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.inventory.InventoryUtil;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VehicleInventory.class)
public interface VehicleInventoryExtender {
    @Shadow
    World getWorld();

    @Redirect(
        method = "readInventoryFromNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/Inventories;readNbt(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/util/collection/DefaultedList;)V"
        )
    )
    private void readInventoryFromNbtUseDynamicRegistry(NbtCompound nbt, DefaultedList<ItemStack> stacks) {
        InventoryUtil.readFromNbt(nbt, this.getWorld().getRegistryManager(), stacks);
    }
}
