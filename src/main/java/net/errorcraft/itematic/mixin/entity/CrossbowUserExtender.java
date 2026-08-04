package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.entity.projectile.ItematicProjectileUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.errorcraft.itematic.item.shooter.method.methods.ChargeableShooterMethod;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(CrossbowAttackMob.class)
public interface CrossbowUserExtender {
    @Redirect(
        method = "performCrossbowAttack(Lnet/minecraft/world/entity/LivingEntity;F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getWeaponHoldingHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/InteractionHand;"
        )
    )
    private InteractionHand getHandPossiblyHoldingForCrossbowUseRegistryKey(LivingEntity entity, Item item) {
        return ItematicProjectileUtil.getHandPossiblyHolding(entity, ItemKeys.CROSSBOW);
    }

    @ModifyConstant(
        method = "performCrossbowAttack",
        constant = @Constant(
            classValue = CrossbowItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfCrossbowItemUseItemComponent(Object reference, Class<CrossbowItem> clazz, @Local ItemStack heldStack, @Share("shooterItemComponent") LocalRef<ShooterItemComponent> shooterItemComponent) {
        Optional<ShooterItemComponent> optionalComponent = heldStack.itematic$getBehavior(ItemComponentTypes.SHOOTER);
        optionalComponent.ifPresent(shooterItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "performCrossbowAttack",
        at = @At("LOAD"),
        ordinal = 0
    )
    private Item castToCrossbowItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "performCrossbowAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CrossbowItem;performShooting(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;FFLnet/minecraft/world/entity/LivingEntity;)V"
        )
    )
    private void shootAllUseItemComponent(CrossbowItem instance, Level world, LivingEntity shooter, InteractionHand hand, ItemStack stack, float speed, float divergence, LivingEntity livingEntity, @Share("shooterItemComponent") LocalRef<ShooterItemComponent> shooterItemComponent) {
        if (shooterItemComponent.get().method() instanceof ChargeableShooterMethod chargeableShooterMethod) {
            chargeableShooterMethod.shoot(shooterItemComponent.get(), world, shooter, hand, stack, speed, divergence, livingEntity);
        }
    }
}
