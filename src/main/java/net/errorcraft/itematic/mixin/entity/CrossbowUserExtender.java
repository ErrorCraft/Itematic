package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.entity.projectile.ItematicProjectileUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(CrossbowUser.class)
public interface CrossbowUserExtender {
    @Redirect(
        method = "shoot(Lnet/minecraft/entity/LivingEntity;F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getHandPossiblyHolding(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/Item;)Lnet/minecraft/util/Hand;"
        )
    )
    private Hand getHandPossiblyHoldingForCrossbowUseRegistryKey(LivingEntity entity, Item item) {
        return ItematicProjectileUtil.getHandPossiblyHolding(entity, ItemKeys.CROSSBOW);
    }

    @ModifyConstant(
        method = "shoot",
        constant = @Constant(
            classValue = CrossbowItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfCrossbowItemUseItemComponent(Object reference, Class<CrossbowItem> clazz, @Local ItemStack heldStack, @Share("shooterItemComponent") LocalRef<ShooterItemComponent> shooterItemComponent) {
        Optional<ShooterItemComponent> optionalComponent = heldStack.itematic$getComponent(ItemComponentTypes.SHOOTER);
        if (optionalComponent.map(ShooterItemComponent::isChargeable).orElse(false)) {
            shooterItemComponent.set(optionalComponent.get());
            return true;
        }
        return false;
    }

    @ModifyVariable(
        method = "shoot",
        at = @At("LOAD"),
        ordinal = 0
    )
    private Item castToCrossbowItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "shoot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/CrossbowItem;shootAll(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;FFLnet/minecraft/entity/LivingEntity;)V"
        )
    )
    private void shootAllUseItemComponent(CrossbowItem instance, World world, LivingEntity shooter, Hand hand, ItemStack stack, float speed, float divergence, LivingEntity livingEntity, @Share("shooterItemComponent") LocalRef<ShooterItemComponent> shooterItemComponent) {
        shooterItemComponent.get().shootAll(world, shooter, hand, stack, speed, divergence, livingEntity);
    }
}
