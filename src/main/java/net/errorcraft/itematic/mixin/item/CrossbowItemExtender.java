package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemExtender {
    @Inject(
        method = "tryLoadProjectiles",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void getAmmunitionUseItemComponent(LivingEntity shooter, ItemStack crossbow, CallbackInfoReturnable<Boolean> info) {
        if (!crossbow.itematic$hasBehavior(ItemComponentTypes.SHOOTER)) {
            info.setReturnValue(false);
        }
    }

    @Redirect(
        method = "tryLoadProjectiles",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack getAmmunitionUseItemComponent(LivingEntity instance, ItemStack stack) {
        if (stack.itematic$hasBehavior(ItemComponentTypes.SHOOTER)) {
            instance.itematic$getAmmunition(stack);
        }

        return ItemStack.EMPTY;
    }

    @Inject(
        method = "getChargeDuration",
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
        method = "getChargeDuration",
        constant = @Constant(
            floatValue = 1.25f
        )
    )
    private static float defaultChargeTimeUseDataComponent(float constant, @Share("defaultChargeTime") LocalFloatRef defaultChargeTime) {
        return defaultChargeTime.get();
    }
}
