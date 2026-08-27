package net.errorcraft.itematic.mixin.world.entity.monster.skeleton;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.entity.projectile.ItematicProjectileUtil;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AbstractSkeleton.class)
public class AbstractSkeletonExtender extends Monster {
    protected AbstractSkeletonExtender(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForBowUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.BOW);
    }

    @ModifyExpressionValue(
        method = "finalizeSpawn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/RandomSource;nextFloat()F"
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.25"
            )
        )
    )
    private float storeItemChance(float original, @Share("randomFloat") LocalFloatRef randomFloat) {
        randomFloat.set(original);
        return original;
    }

    @Redirect(
        method = "finalizeSpawn",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item, ServerLevelAccessor level, @Share("randomFloat") LocalFloatRef randomFloat) {
        if (randomFloat.get() < 0.1f) {
            return level.itematic$createStack(ItemIds.JACK_O_LANTERN);
        }

        return level.itematic$createStack(ItemIds.CARVED_PUMPKIN);
    }

    @Redirect(
        method = {
            "reassessWeaponGoal",
            "performRangedAttack"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getWeaponHoldingHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/InteractionHand;"
        )
    )
    private InteractionHand getHandForHeldBowUseId(LivingEntity mob, Item weaponItem) {
        return ItematicProjectileUtil.getWeaponHoldingHand(mob, ItemIds.BOW);
    }

    @WrapMethod(
        method = "canUseNonMeleeWeapon"
    )
    private boolean checkShooterMethod(ItemStack item, Operation<Boolean> original) {
        return item.itematic$getBehavior(ItemBehaviorType.SHOOTER)
            .map(shooter -> shooter.usesMethod(ShooterMethodType.DIRECT))
            .orElse(false);
    }

    @Redirect(
        method = "reassessWeaponGoal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBowCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.BOW);
    }

    @Redirect(
        method = "performRangedAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileUsingShoot(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;DDDFF)Lnet/minecraft/world/entity/projectile/Projectile;"
        )
    )
    private <T extends Projectile> T onlySetSpeed(T projectile, ServerLevel serverLevel, ItemStack itemStack, double targetX, double targetY, double targetZ, float pow, float uncertainty) {
        projectile.shoot(targetX, targetY, targetZ, pow, uncertainty);
        return projectile;
    }
}
