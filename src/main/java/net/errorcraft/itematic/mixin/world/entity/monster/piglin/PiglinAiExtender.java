package net.errorcraft.itematic.mixin.world.entity.monster.piglin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.Chargeable;
import net.minecraft.references.ItemIds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PiglinAi.class)
public class PiglinAiExtender {
    @WrapOperation(
        method = {
            "pickUpItem",
            "wantsToPickup"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isGoldNuggetCheckId(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.is(ItemIds.GOLD_NUGGET);
    }

    @WrapOperation(
        method = "isBarterCurrency",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isGoldIngotCheckId(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.is(ItemIds.GOLD_INGOT);
    }

    @WrapOperation(
        method = "hasCrossbow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private static boolean isHoldingCrossbowUseDataComponent(LivingEntity instance, Item item, Operation<Boolean> original) {
        return instance.isHolding(stack -> stack.get(ItematicDataComponents.SHOOTER_METHOD) == Chargeable.INSTANCE);
    }
}
