package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.SmashingWeaponDataComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(MaceItem.class)
public class MaceItemExtender {
    @Unique
    private static SmashingWeaponDataComponent usedStackSmashingWeaponDataComponent;

    @Inject(
        method = "postHit",
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

    @Redirect(
        method = "postHit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/MaceItem;shouldDealAdditionalDamage(Lnet/minecraft/entity/LivingEntity;)Z"
        )
    )
    private boolean shouldDealAdditionalDamageUseDataComponent(LivingEntity attacker, @Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        return smashingWeapon.get().canSmash(attacker);
    }

    @ModifyConstant(
        method = "postHit",
        constant = @Constant(
            doubleValue = 5.0d
        )
    )
    private double heavySmashAttackFallDistanceUseDataComponent(double constant, @Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        return smashingWeapon.get().heavySmashAttackFallDistance();
    }

    @Redirect(
        method = "postHit",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/sound/SoundEvents;ITEM_MACE_SMASH_GROUND_HEAVY:Lnet/minecraft/sound/SoundEvent;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private SoundEvent getOnGroundLargeFallDistanceSoundUseDataComponent(@Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        return smashingWeapon.get()
            .hitSounds()
            .onGroundLargeFallDistance()
            .value();
    }

    @Redirect(
        method = "postHit",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/sound/SoundEvents;ITEM_MACE_SMASH_GROUND:Lnet/minecraft/sound/SoundEvent;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private SoundEvent getOnGroundSmallFallDistanceSoundUseDataComponent(@Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        return smashingWeapon.get()
            .hitSounds()
            .onGroundLargeFallDistance()
            .value();
    }

    @ModifyArg(
        method = "postHit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;playSound(Lnet/minecraft/entity/Entity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
        )
    )
    private SoundEvent getInAirSoundUseDataComponent(SoundEvent sound, @Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        return smashingWeapon.get()
            .hitSounds()
            .inAir()
            .value();
    }

    @WrapOperation(
        method = "postHit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/MaceItem;knockbackNearbyEntities(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)V"
        )
    )
    private void temporarilyStoreUsedStack(World world, Entity attacker, Entity attacked, Operation<Void> original, @Share("smashingWeapon") LocalRef<SmashingWeaponDataComponent> smashingWeapon) {
        usedStackSmashingWeaponDataComponent = smashingWeapon.get();
        original.call(world, attacker, attacker);
        usedStackSmashingWeaponDataComponent = null;
    }

    @Redirect(
        method = "postDamageEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/MaceItem;shouldDealAdditionalDamage(Lnet/minecraft/entity/LivingEntity;)Z"
        )
    )
    private boolean shouldDealAdditionalDamageUseDataComponent(LivingEntity attacker, ItemStack stack) {
        SmashingWeaponDataComponent smashingWeapon = stack.get(ItematicDataComponentTypes.SMASHING_WEAPON);
        if (smashingWeapon == null) {
            return false;
        }

        return smashingWeapon.canSmash(attacker);
    }

    @Redirect(
        method = "getBonusAttackDamage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/MaceItem;shouldDealAdditionalDamage(Lnet/minecraft/entity/LivingEntity;)Z"
        )
    )
    private boolean shouldDealAdditionalDamageUseDataComponent(LivingEntity attacker) {
        SmashingWeaponDataComponent smashingWeapon = Objects.requireNonNull(attacker.getWeaponStack())
            .get(ItematicDataComponentTypes.SMASHING_WEAPON);
        if (smashingWeapon == null) {
            return false;
        }

        return smashingWeapon.canSmash(attacker);
    }

    @ModifyConstant(
        method = {
            "method_58409",
            "getKnockback"
        },
        constant = @Constant(
            doubleValue = 0.699999988079071d
        )
    )
    private static double knockbackPowerUseDataComponent(double constant) {
        return usedStackSmashingWeaponDataComponent.knockbackPower();
    }

    @ModifyConstant(
        method = "getKnockback",
        constant = @Constant(
            doubleValue = 5.0d
        )
    )
    private static double heavySmashAttackFallDistanceUseDataComponent(double constant) {
        return usedStackSmashingWeaponDataComponent.heavySmashAttackFallDistance();
    }
}
