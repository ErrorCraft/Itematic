package net.errorcraft.itematic.mixin.client.render.item;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.shooter.method.methods.ChargeableShooterMethod;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.OptionalInt;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererExtender {
    @Redirect(
        method = {
            "getHandRenderType",
            "getUsingItemHandRenderType"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;BOW:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            ),
            to = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CROSSBOW:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static boolean isOfForBowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BOW);
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CROSSBOW:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForCrossbowUseItemComponent(ItemStack instance, Item item, AbstractClientPlayerEntity player, @Share("useDuration") LocalIntRef useDuration) {
        OptionalInt optionalUseDuration = instance.itematic$getComponent(ItemComponentTypes.SHOOTER)
            .map(shooter -> shooter.useDuration(instance, player))
            .orElseGet(OptionalInt::empty);
        if (optionalUseDuration.orElse(UseDurationDataComponent.INDEFINITE_USE_DURATION) == UseDurationDataComponent.INDEFINITE_USE_DURATION) {
            return false;
        }

        useDuration.set(optionalUseDuration.getAsInt());
        return true;
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getItemUseTimeLeft()I",
            ordinal = 0
        )
    )
    private int useDifference(AbstractClientPlayerEntity instance, @Share("useDuration") LocalIntRef useDuration) {
        return useDuration.get() - instance.itematic$itemUsedTicks();
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getMaxUseTime(Lnet/minecraft/entity/LivingEntity;)I"
        )
    )
    private int getMaxUseTimeReturnZero(ItemStack instance, LivingEntity livingEntity) {
        return 0;
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getItemUseTimeLeft()I"
        )
    )
    private int getUseTimeLeftUseUsedTicks(AbstractClientPlayerEntity instance) {
        return -instance.itematic$itemUsedTicks();
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)I"
        )
    )
    private int getPullTimeUseItemComponent(ItemStack stack, LivingEntity user) {
        return ChargeableShooterMethod.getChargeTime(stack, user);
    }

    @Redirect(
        method = {
            "getHandRenderType",
            "getUsingItemHandRenderType",
            "isChargedCrossbow"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CROSSBOW:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static boolean isOfForCrossbowUseRegistryKeyCheckStatic(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.CROSSBOW);
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;FILLED_MAP:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForFilledMapUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasComponent(ItemComponentTypes.MAP_HOLDER);
    }
}
