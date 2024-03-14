package net.errorcraft.itematic.mixin.entity.projectile;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
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
        method = "getItem",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireChargeUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.FIRE_CHARGE);
    }
}
