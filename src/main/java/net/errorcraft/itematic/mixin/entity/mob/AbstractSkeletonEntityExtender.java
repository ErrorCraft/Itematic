package net.errorcraft.itematic.mixin.entity.mob;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodTypes;
import net.errorcraft.itematic.world.entity.projectile.ItematicProjectileUtil;
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
public class AbstractSkeletonEntityExtender extends Monster {
    protected AbstractSkeletonEntityExtender(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForBowUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.BOW);
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
    private ItemStack newItemStackUseCreateStack(ItemLike item, ServerLevelAccessor world, @Share("randomFloat") LocalFloatRef randomFloat) {
        if (randomFloat.get() < 0.1f) {
            return world.itematic$createStack(ItemKeys.JACK_O_LANTERN);
        }
        return world.itematic$createStack(ItemKeys.CARVED_PUMPKIN);
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
    private InteractionHand getHandPossiblyHoldingForBowUseRegistryKey(LivingEntity entity, Item item) {
        return ItematicProjectileUtil.getWeaponHoldingHand(entity, ItemKeys.BOW);
    }

    @ModifyReturnValue(
        method = "canUseNonMeleeWeapon",
        at = @At("TAIL")
    )
    private boolean useItemBehaviorComponent(boolean original, ItemStack stack) {
        return stack.itematic$getBehavior(ItemComponentTypes.SHOOTER)
            .map(shooter -> shooter.usesMethod(ShooterMethodTypes.DIRECT))
            .orElse(false);
    }

    @Redirect(
        method = "reassessWeaponGoal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForBowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BOW);
    }

    @Redirect(
        method = "performRangedAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectileUsingShoot(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;DDDFF)Lnet/minecraft/world/entity/projectile/Projectile;"
        )
    )
    private <T extends Projectile> T onlySetSpeed(T projectile, ServerLevel world, ItemStack projectileStack, double velocityX, double velocityY, double velocityZ, float power, float divergence) {
        projectile.shoot(velocityX, velocityY, velocityZ, power, divergence);
        return projectile;
    }
}
