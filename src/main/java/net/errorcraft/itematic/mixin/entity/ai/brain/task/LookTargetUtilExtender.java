package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(LookTargetUtil.class)
public class LookTargetUtilExtender {
    @ModifyConstant(
        method = "isTargetWithinAttackRange",
        constant = @Constant(
            classValue = RangedWeaponItem.class,
            ordinal = 0
        )
    )
    private static boolean instanceOfRangedWeaponItemUseItemComponentCheck(Object reference, Class<RangedWeaponItem> clazz, MobEntity mob, @Share("shooterItemComponent") LocalRef<ShooterItemComponent> shooterItemComponent) {
        Optional<ShooterItemComponent> optionalComponent = mob.getMainHandStack().itematic$getComponent(ItemComponentTypes.SHOOTER);
        optionalComponent.ifPresent(shooterItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "isTargetWithinAttackRange",
        at = @At("LOAD"),
        ordinal = 0
    )
    private static Item castToRangedWeaponItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "isTargetWithinAttackRange",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/MobEntity;canUseRangedWeapon(Lnet/minecraft/item/RangedWeaponItem;)Z"
        )
    )
    private static boolean canUseRangedWeaponUseItemComponent(MobEntity instance, RangedWeaponItem weapon, @Share("shooterItemComponent") LocalRef<ShooterItemComponent> shooterItemComponent) {
        return instance.itematic$canUseShooter(instance.getMainHandStack(), shooterItemComponent.get());
    }

    @Redirect(
        method = "isTargetWithinAttackRange",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/RangedWeaponItem;getRange()I"
        )
    )
    private static int getRangeUseItemComponent(RangedWeaponItem instance, @Share("shooterItemComponent") LocalRef<ShooterItemComponent> shooterItemComponent) {
        return shooterItemComponent.get().range();
    }
}
