package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.SmashingWeaponDataComponent;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(MaceItem.class)
public class MaceItemExtender {
    @Unique
    private static SmashingWeaponDataComponent usedStackSmashingWeaponDataComponent;

    @Inject(
        method = "hurtEnemy",
        at = @At("HEAD"),
        cancellable = true
    )
    private void storeSmashingWeaponDataComponent(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfo info, @Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        SmashingWeaponDataComponent smashingWeaponDataComponent = stack.get(ItematicDataComponentTypes.SMASHING_WEAPON);
        if (smashingWeaponDataComponent == null) {
            info.cancel();
            return;
        }

        smashingWeapon.set(smashingWeaponDataComponent);
    }

    @WrapOperation(
        method = "hurtEnemy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/MaceItem;canSmashAttack(Lnet/minecraft/world/entity/LivingEntity;)Z"
        )
    )
    private boolean shouldDealAdditionalDamageUseDataComponent(LivingEntity attacker, Operation<Boolean> original, @Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        return smashingWeapon.get().canSmash(attacker);
    }

    @ModifyConstant(
        method = "hurtEnemy",
        constant = @Constant(
            doubleValue = 5.0d
        )
    )
    private double heavySmashAttackFallDistanceUseDataComponent(double constant, @Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        return smashingWeapon.get().heavySmashAttackFallDistance();
    }

    @WrapOperation(
        method = "hurtEnemy",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/sounds/SoundEvents;MACE_SMASH_GROUND_HEAVY:Lnet/minecraft/sounds/SoundEvent;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private SoundEvent getOnGroundLargeFallDistanceSoundUseDataComponent(Operation<SoundEvent> original, @Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        return smashingWeapon.get()
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
    private SoundEvent getOnGroundSmallFallDistanceSoundUseDataComponent(Operation<SoundEvent> original, @Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        return smashingWeapon.get()
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
    private SoundEvent getInAirSoundUseDataComponent(Operation<SoundEvent> original, @Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        return smashingWeapon.get()
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
    private void temporarilyStoreUsedStack(Level world, Entity attacker, Entity attacked, Operation<Void> original, @Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        usedStackSmashingWeaponDataComponent = smashingWeapon.get();
        original.call(world, attacker, attacker);
        usedStackSmashingWeaponDataComponent = null;
    }

    @WrapOperation(
        method = "postHurtEnemy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/MaceItem;canSmashAttack(Lnet/minecraft/world/entity/LivingEntity;)Z"
        )
    )
    private boolean shouldDealAdditionalDamageUseDataComponent(LivingEntity attacker, Operation<Boolean> original, ItemStack stack) {
        SmashingWeaponDataComponent smashingWeapon = stack.get(ItematicDataComponentTypes.SMASHING_WEAPON);
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
    private boolean shouldDealAdditionalDamageUseDataComponent(LivingEntity attacker, Operation<Boolean> original) {
        SmashingWeaponDataComponent smashingWeapon = Objects.requireNonNull(attacker.getWeaponItem())
            .get(ItematicDataComponentTypes.SMASHING_WEAPON);
        if (smashingWeapon == null) {
            return false;
        }

        return smashingWeapon.canSmash(attacker);
    }

    @ModifyConstant(
        method = {
            "method_58409",
            "getKnockbackPower"
        },
        constant = @Constant(
            doubleValue = 0.699999988079071d
        )
    )
    private static double knockbackPowerUseDataComponent(double constant) {
        return usedStackSmashingWeaponDataComponent.knockbackPower();
    }

    @ModifyConstant(
        method = "getKnockbackPower",
        constant = @Constant(
            doubleValue = 5.0d
        )
    )
    private static double heavySmashAttackFallDistanceUseDataComponent(double constant) {
        return usedStackSmashingWeaponDataComponent.heavySmashAttackFallDistance();
    }
}
