package net.errorcraft.itematic.mixin.entity.ai.brain.task;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.minecraft.entity.ai.brain.task.MeleeAttackTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(MeleeAttackTask.class)
public class MeleeAttackTaskExtender {
    @ModifyConstant(
        method = "method_25943",
        constant = @Constant(
            classValue = RangedWeaponItem.class,
            ordinal = 0
        )
    )
    private static boolean instanceOfRangedWeaponItemUseItemComponentCheck(Object reference, Class<RangedWeaponItem> clazz, MobEntity mob, @Local(argsOnly = true) ItemStack stack, @Share("shooterItemComponent") LocalRef<ShooterItemComponent> shooterItemComponent) {
        Optional<ShooterItemComponent> optionalComponent = stack.itematic$getBehavior(ItemComponentTypes.SHOOTER);
        optionalComponent.ifPresent(shooterItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "method_25943",
        at = @At("LOAD"),
        ordinal = 0
    )
    private static Item castToRangedWeaponItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "method_25943",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/MobEntity;canUseRangedWeapon(Lnet/minecraft/item/RangedWeaponItem;)Z"
        )
    )
    private static boolean canUseRangedWeaponUseItemComponent(MobEntity instance, RangedWeaponItem weapon, @Local(argsOnly = true) ItemStack stack, @Share("shooterItemComponent") LocalRef<ShooterItemComponent> shooterItemComponent) {
        return instance.itematic$canUseShooter(stack, shooterItemComponent.get());
    }
}
