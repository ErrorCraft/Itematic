package net.errorcraft.itematic.mixin.entity.mob;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.errorcraft.itematic.access.entity.mob.MobEntityAccess;
import net.errorcraft.itematic.entity.projectile.ItematicProjectileUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AbstractSkeletonEntity.class)
public class AbstractSkeletonEntityExtender extends HostileEntity implements MobEntityAccess {
    protected AbstractSkeletonEntityExtender(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForBowUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.BOW);
    }

    @ModifyExpressionValue(
        method = "initialize",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"
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
        method = "initialize",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemConvertible item, ServerWorldAccess world, @Share("randomFloat") LocalFloatRef randomFloat) {
        if (randomFloat.get() < 0.1f) {
            return world.itematic$createStack(ItemKeys.JACK_O_LANTERN);
        }
        return world.itematic$createStack(ItemKeys.CARVED_PUMPKIN);
    }

    @Redirect(
        method = { "updateAttackType", "shootAt" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;"
        )
    )
    private Hand getHandPossiblyHoldingForBowUseRegistryKey(LivingEntity entity, Item item) {
        return ItematicProjectileUtil.getHandPossiblyHolding(entity, ItemKeys.BOW);
    }

    @Redirect(
        method = "updateAttackType",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BOW);
    }

    @Override
    public boolean itematic$canUseShooter(ItemStack stack, ShooterItemComponent component) {
        return stack.itematic$isOf(ItemKeys.BOW);
    }
}
