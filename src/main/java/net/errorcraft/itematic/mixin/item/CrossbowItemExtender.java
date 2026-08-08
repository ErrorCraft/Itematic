package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemExtender {
    @Inject(
        method = "getChargeDuration",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void checkAndStoreDefaultChargeTime(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> info, @Share("defaultChargeTime") LocalFloatRef defaultChargeTime) {
        Float possibleDefaultChargeTime = stack.get(ItematicDataComponents.SHOOTER_DEFAULT_CHARGE_TIME);
        if (possibleDefaultChargeTime == null) {
            info.setReturnValue(0);
            return;
        }

        defaultChargeTime.set(possibleDefaultChargeTime);
    }

    @ModifyConstant(
        method = "getChargeDuration",
        constant = @Constant(
            floatValue = 1.25f
        )
    )
    private static float defaultChargeTimeUseDataComponent(float constant, @Share("defaultChargeTime") LocalFloatRef defaultChargeTime) {
        return defaultChargeTime.get();
    }
}
