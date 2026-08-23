package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.weapon.melee.SmashingWeapon;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MaceItem.class)
public class MaceItemExtender {
    @Unique
    private static SmashingWeapon usedStackSmashingWeapon;

    @WrapMethod(
        method = "hurtEnemy"
    )
    private void storeSmashingWeapon(ItemStack itemStack, LivingEntity mob, LivingEntity attacker, Operation<Void> original, @Share("smashingWeapon") LocalRef<SmashingWeapon> smashingWeaponReference) {
        SmashingWeapon smashingWeapon = itemStack.get(ItematicDataComponents.SMASHING_WEAPON);
        if (smashingWeapon == null) {
            return;
        }

        smashingWeaponReference.set(smashingWeapon);
        original.call(itemStack, mob, attacker);
    }

    @WrapOperation(
        method = "hurtEnemy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/MaceItem;canSmashAttack(Lnet/minecraft/world/entity/LivingEntity;)Z"
        )
    )
    private boolean canSmashAttackUseDataComponent(LivingEntity attacker, Operation<Boolean> original, @Share("smashingWeapon") LocalRef<SmashingWeapon> smashingWeaponReference) {
        return smashingWeaponReference.get().canSmash(attacker);
    }

    @ModifyConstant(
        method = "hurtEnemy",
        constant = @Constant(
            doubleValue = 5.0d
        )
    )
    private double heavySmashAttackFallDistanceUseDataComponent(double constant, @Share("smashingWeapon") LocalRef<SmashingWeapon> smashingWeaponReference) {
        return smashingWeaponReference.get().heavySmashAttackFallDistance();
    }

    @WrapOperation(
        method = "hurtEnemy",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/sounds/SoundEvents;MACE_SMASH_GROUND_HEAVY:Lnet/minecraft/sounds/SoundEvent;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private SoundEvent getOnGroundLargeFallDistanceSoundUseDataComponent(Operation<SoundEvent> original, @Share("smashingWeapon") LocalRef<SmashingWeapon> smashingWeaponReference) {
        return smashingWeaponReference.get()
            .hitSounds()
            .onGroundLargeFallDistance()
            .value();
    }

    @WrapOperation(
        method = "hurtEnemy",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/sounds/SoundEvents;MACE_SMASH_GROUND:Lnet/minecraft/sounds/SoundEvent;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private SoundEvent getOnGroundSmallFallDistanceSoundUseDataComponent(Operation<SoundEvent> original, @Share("smashingWeapon") LocalRef<SmashingWeapon> smashingWeaponReference) {
        return smashingWeaponReference.get()
            .hitSounds()
            .onGroundSmallFallDistance()
            .value();
    }

    @WrapOperation(
        method = "hurtEnemy",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/sounds/SoundEvents;MACE_SMASH_AIR:Lnet/minecraft/sounds/SoundEvent;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private SoundEvent getInAirSoundUseDataComponent(Operation<SoundEvent> original, @Share("smashingWeapon") LocalRef<SmashingWeapon> smashingWeaponReference) {
        return smashingWeaponReference.get()
            .hitSounds()
            .inAir()
            .value();
    }

    @WrapOperation(
        method = "hurtEnemy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/MaceItem;knockback(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;)V"
        )
    )
    private void temporarilyStoreSmashingWeapon(Level level, Entity attacker, Entity entity, Operation<Void> original, @Share("smashingWeapon") LocalRef<SmashingWeapon> smashingWeaponReference) {
        usedStackSmashingWeapon = smashingWeaponReference.get();
        original.call(level, attacker, attacker);
        usedStackSmashingWeapon = null;
    }

    @WrapOperation(
        method = "postHurtEnemy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/MaceItem;canSmashAttack(Lnet/minecraft/world/entity/LivingEntity;)Z"
        )
    )
    private boolean canSmashAttackUseDataComponent(LivingEntity attacker, Operation<Boolean> original, ItemStack itemStack) {
        SmashingWeapon smashingWeapon = itemStack.get(ItematicDataComponents.SMASHING_WEAPON);
        if (smashingWeapon == null) {
            return false;
        }

        return smashingWeapon.canSmash(attacker);
    }

    @WrapOperation(
        method = "getAttackDamageBonus",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/MaceItem;canSmashAttack(Lnet/minecraft/world/entity/LivingEntity;)Z"
        )
    )
    private boolean canSmashAttackUseDataComponent(LivingEntity attacker, Operation<Boolean> original) {
        SmashingWeapon smashingWeapon = attacker.getWeaponItem()
            .get(ItematicDataComponents.SMASHING_WEAPON);
        if (smashingWeapon == null) {
            return false;
        }

        return smashingWeapon.canSmash(attacker);
    }

    @ModifyConstant(
        method = {
            "lambda$knockback$0",
            "getKnockbackPower"
        },
        constant = @Constant(
            doubleValue = 0.699999988079071d
        )
    )
    private static double knockbackPowerUseDataComponent(double constant) {
        return usedStackSmashingWeapon.knockbackPower();
    }

    @ModifyConstant(
        method = "getKnockbackPower",
        constant = @Constant(
            doubleValue = 5.0d
        )
    )
    private static double heavySmashAttackFallDistanceUseDataComponent(double constant) {
        return usedStackSmashingWeapon.heavySmashAttackFallDistance();
    }
}
