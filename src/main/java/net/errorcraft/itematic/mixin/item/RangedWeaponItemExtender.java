package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ProjectileItemComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemExtender {
    @Redirect(
        method = "createArrowEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"
        )
    )
    private PersistentProjectileEntity createArrowUseItemComponent(ArrowItem instance, World world, ItemStack projectile, LivingEntity shooter) {
        Optional<ProjectileItemComponent> component = projectile.itematic$getComponent(ItemComponentTypes.PROJECTILE);
        if (component.isEmpty()) {
            return null;
        }

        Entity entity = component.get().createEntity(world, shooter, projectile, 0.0f, 1.0f);
        if (entity instanceof PersistentProjectileEntity persistentProjectileEntity) {
            return persistentProjectileEntity;
        }

        world.spawnEntity(entity);
        return null;
    }

    @Inject(
        method = "createArrowEntity",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void createArrowCheckNullEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical, CallbackInfoReturnable<ProjectileEntity> info, @Local PersistentProjectileEntity persistentProjectileEntity) {
        if (persistentProjectileEntity == null) {
            info.setReturnValue(null);
        }
    }
}
