package net.errorcraft.itematic.mixin.inventory;

import net.errorcraft.itematic.access.inventory.EnderChestInventoryAccess;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderChestInventory.class)
public class EnderChestInventoryExtender extends SimpleInventory implements EnderChestInventoryAccess {
    @Unique
    private PlayerEntity player;

    @Redirect(
        method = "readNbtList",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack fromNbtUseDynamicRegistry(NbtCompound nbt) {
        return ItemStackUtil.fromNbt(nbt, this.player.getWorld().getRegistryManager());
    }

    @Override
    public void itematic$setPlayer(PlayerEntity player) {
        this.player = player;
    }
}
