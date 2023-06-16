package net.errorcraft.itematic.mixin.entity.projectile;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractFireballEntity.class)
public class AbstractFireballEntityExtender extends ExplosiveProjectileEntity {
    protected AbstractFireballEntityExtender(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "setItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean setItemIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.FIRE_CHARGE);
    }

    @Redirect(
        method = "getStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack getStackNewItemStackUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.FIRE_CHARGE));
    }
}
