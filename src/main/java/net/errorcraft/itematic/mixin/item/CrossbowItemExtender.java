package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemExtender {
    @Inject(
        method = "loadProjectiles",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void getAmmunitionUseItemComponent(LivingEntity shooter, ItemStack crossbow, CallbackInfoReturnable<Boolean> info) {
        if (!crossbow.itematic$hasComponent(ItemComponentTypes.SHOOTER)) {
            info.setReturnValue(false);
        }
    }

    @Redirect(
        method = "loadProjectiles",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;getProjectileType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack getAmmunitionUseItemComponent(LivingEntity instance, ItemStack stack) {
        if (stack.itematic$hasComponent(ItemComponentTypes.SHOOTER)) {
            instance.itematic$getAmmunition(stack);
        }

        return ItemStack.EMPTY;
    }

    @Inject(
        method = "getPullTime",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void checkAndStoreDefaultChargeTime(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> info, @Share("defaultChargeTime") LocalFloatRef defaultChargeTime) {
        Float possibleDefaultChargeTime = stack.get(ItematicDataComponentTypes.SHOOTER_DEFAULT_CHARGE_TIME);
        if (possibleDefaultChargeTime == null) {
            info.setReturnValue(0);
            return;
        }

        defaultChargeTime.set(possibleDefaultChargeTime);
    }

    @ModifyConstant(
        method = "getPullTime",
        constant = @Constant(
            floatValue = 1.25f
        )
    )
    private static float defaultChargeTimeUseDataComponent(float constant, @Share("defaultChargeTime") LocalFloatRef defaultChargeTime) {
        return defaultChargeTime.get();
    }
}
