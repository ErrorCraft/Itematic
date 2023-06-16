package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombieEntity.class)
public class ZombieEntityExtender extends HostileEntity {
    protected ZombieEntityExtender(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "canPickupItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean canPickupItemIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.EGG);
    }

    @Redirect(
        method = "getSkull",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack getSkullNewItemStackUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.ZOMBIE_HEAD));
    }
}
