package net.errorcraft.itematic.mixin.world.entity.monster;

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
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(CrossbowAttackMob.class)
public interface CrossbowAttackMobExtender {
    @Redirect(
        method = "performCrossbowAttack(Lnet/minecraft/world/entity/LivingEntity;F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getWeaponHoldingHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/InteractionHand;"
        )
    )
    private InteractionHand getHandForHeldCrossbowUseId(LivingEntity mob, Item weaponItem) {
        return ItematicProjectileUtil.getWeaponHoldingHand(mob, ItemIds.CROSSBOW);
    }

    @ModifyConstant(
        method = "performCrossbowAttack",
        constant = @Constant(
            classValue = CrossbowItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfCrossbowItemUseItemBehavior(Object reference, Class<CrossbowItem> clazz, @Local(name = "usedItem") ItemStack usedItem, @Share("shooter") LocalRef<ShooterItemBehavior> shooterReference) {
        Optional<ShooterItemBehavior> optionalShooter = usedItem.itematic$getBehavior(ItemBehaviorType.SHOOTER);
        optionalShooter.ifPresent(shooterReference::set);
        return optionalShooter.isPresent();
    }

    @ModifyVariable(
        method = "performCrossbowAttack",
        at = @At("LOAD"),
        ordinal = 0
    )
    @Nullable
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
    private void performShootingUseItemBehavior(CrossbowItem instance, Level level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, float power, float uncertainty, LivingEntity targetOverride, @Share("shooter") LocalRef<ShooterItemBehavior> shooterReference) {
        if (shooterReference.get().method() instanceof ChargeableShooterMethod chargeable) {
            chargeable.shoot(shooterReference.get(), level, shooter, hand, weapon, power, uncertainty, targetOverride);
        }
    }
}
