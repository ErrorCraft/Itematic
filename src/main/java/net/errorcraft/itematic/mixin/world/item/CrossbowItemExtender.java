package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CrossbowItem.class)
public class CrossbowItemExtender {
    @WrapMethod(
        method = "getChargeDuration"
    )
    private static int checkAndStoreDefaultChargeTime(ItemStack crossbow, LivingEntity user, Operation<Integer> original, @Share("defaultChargeTime") LocalFloatRef defaultChargeTimeReference) {
        Float defaultChargeTime = crossbow.get(ItematicDataComponents.SHOOTER_DEFAULT_CHARGE_TIME);
        if (defaultChargeTime == null) {
            return 0;
        }

        defaultChargeTimeReference.set(defaultChargeTime);
        return original.call(crossbow, user);
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
