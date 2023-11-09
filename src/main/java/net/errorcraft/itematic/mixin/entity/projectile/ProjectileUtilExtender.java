package net.errorcraft.itematic.mixin.entity.projectile;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilExtender {
    @Redirect(
        method = "createArrowProjectile",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"
        )
    )
    private static PersistentProjectileEntity createArrowProjectileCreateArrowUseItemComponent(ArrowItem instance, World world, ItemStack projectile, LivingEntity shooter) {
        Optional<Entity> optional = projectile.itematic$getComponent(ItemComponentTypes.PROJECTILE)
            .map(c -> c.createEntity(world, shooter, projectile, 1.0f, 1.0f));
        if (optional.isEmpty()) {
            return new ArrowEntity(world, shooter);
        }

        if (optional.get() instanceof PersistentProjectileEntity persistentProjectileEntity) {
            return persistentProjectileEntity;
        }
        return new ArrowEntity(world, shooter);
    }

    @Redirect(
        method = "createArrowProjectile",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean createArrowProjectileIsOfAlwaysReturnTrue(ItemStack instance, Item item) {
        return true;
    }
}
