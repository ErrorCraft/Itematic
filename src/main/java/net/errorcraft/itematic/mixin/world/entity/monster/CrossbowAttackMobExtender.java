package net.errorcraft.itematic.mixin.world.entity.monster;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.entity.projectile.Projectiles;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.Chargeable;
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

@Mixin(CrossbowAttackMob.class)
public interface CrossbowAttackMobExtender {
    @WrapOperation(
        method = "performCrossbowAttack(Lnet/minecraft/world/entity/LivingEntity;F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getWeaponHoldingHand(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/InteractionHand;"
        )
    )
    private InteractionHand getWeaponHoldingHandForCrossbowUseDataComponent(LivingEntity mob, Item weaponItem, Operation<InteractionHand> original) {
        return Projectiles.getWeaponHoldingHand(mob, Chargeable.INSTANCE);
    }

    @ModifyConstant(
        method = "performCrossbowAttack",
        constant = @Constant(
            classValue = CrossbowItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfCrossbowItemUseDataComponent(Object reference, Class<CrossbowItem> clazz, @Local(name = "usedItem") ItemStack usedItem) {
        return usedItem.get(ItematicDataComponents.SHOOTER_METHOD) == Chargeable.INSTANCE;
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

    @WrapOperation(
        method = "performCrossbowAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CrossbowItem;performShooting(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;FFLnet/minecraft/world/entity/LivingEntity;)V"
        )
    )
    private void performShootingUseDataComponent(CrossbowItem instance, Level level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, float power, float uncertainty, LivingEntity targetOverride, Operation<Void> original) {
        Chargeable.INSTANCE.shoot(level, shooter, hand, weapon, power, uncertainty, targetOverride);
    }
}
