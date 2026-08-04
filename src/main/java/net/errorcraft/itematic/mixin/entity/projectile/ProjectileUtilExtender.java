package net.errorcraft.itematic.mixin.entity.projectile;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilExtender {
    @Redirect(
        method = "getMobArrow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ArrowItem;createArrow(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;"
        )
    )
    private static AbstractArrow createProjectileUseItemComponent(ArrowItem instance, Level world, ItemStack projectile, LivingEntity shooter, ItemStack shotFrom) {
        Entity entity = projectile.itematic$getBehavior(ItemComponentTypes.PROJECTILE)
            .map(projectileBehavior -> projectileBehavior.spawnEntity(world, shooter, projectile, 1.0f, 1.0f))
            .orElse(null);
        if (entity instanceof AbstractArrow persistentProjectileEntity) {
            return persistentProjectileEntity;
        }

        return new Arrow(world, shooter, projectile.copyWithCount(1), shotFrom);
    }
}
