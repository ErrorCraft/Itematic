package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.entity.projectile.ItematicProjectileUtil;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.ShooterItemBehavior;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.ChargeableShooterMethod;
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
        return ItematicProjectileUtil.getWeaponHoldingHand(entity, ItemIds.CROSSBOW);
    }

    @ModifyConstant(
        method = "performCrossbowAttack",
        constant = @Constant(
            classValue = CrossbowItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfCrossbowItemUseItemBehavior(Object reference, Class<CrossbowItem> clazz, @Local ItemStack heldStack, @Share("shooter") LocalRef<ShooterItemBehavior> shooterReference) {
        Optional<ShooterItemBehavior> optionalShooter = heldStack.itematic$getBehavior(ItemBehaviorType.SHOOTER);
        optionalShooter.ifPresent(shooterReference::set);
        return optionalShooter.isPresent();
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
    private void shootAllUseItemBehavior(CrossbowItem instance, Level world, LivingEntity shooter, InteractionHand hand, ItemStack stack, float speed, float divergence, LivingEntity livingEntity, @Share("shooter") LocalRef<ShooterItemBehavior> shooterReference) {
        if (shooterReference.get().method() instanceof ChargeableShooterMethod chargeableShooterMethod) {
            chargeableShooterMethod.shoot(shooterReference.get(), world, shooter, hand, stack, speed, divergence, livingEntity);
        }
    }
}
