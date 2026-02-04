package net.errorcraft.itematic.mixin.entity.projectile;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilExtender {
    @Redirect(
        method = "createArrowProjectile",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"
        )
    )
    private static PersistentProjectileEntity createProjectileUseItemComponent(ArrowItem instance, World world, ItemStack projectile, LivingEntity shooter, ItemStack shotFrom) {
        Entity entity = projectile.itematic$getBehavior(ItemComponentTypes.PROJECTILE)
            .map(c -> c.createEntity(world, shooter, projectile, 1.0f, 1.0f))
            .orElse(null);
        if (entity instanceof PersistentProjectileEntity persistentProjectileEntity) {
            return persistentProjectileEntity;
        }

        return new ArrowEntity(world, shooter, projectile.copyWithCount(1), shotFrom);
    }
}
