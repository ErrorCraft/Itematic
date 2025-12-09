package net.errorcraft.itematic.mixin.client.render.item;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Optional;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererExtender {
    @Shadow
    @Final
    private MinecraftClient client;

    @Redirect(
        method = { "getHandRenderType", "getUsingItemHandRenderType" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;BOW:Lnet/minecraft/item/Item;"
            ),
            to = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CROSSBOW:Lnet/minecraft/item/Item;"
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
                target = "Lnet/minecraft/item/Items;CROSSBOW:Lnet/minecraft/item/Item;"
            )
        )
    )
    private boolean isOfForCrossbowUseItemComponent(ItemStack instance, Item item, @Share("shooterItemComponent") LocalRef<ShooterItemComponent> shooterItemComponent) {
        Optional<ShooterItemComponent> optionalComponent = instance.itematic$getComponent(ItemComponentTypes.SHOOTER);
        optionalComponent.ifPresent(shooterItemComponent::set);
        return optionalComponent.map(ShooterItemComponent::isChargeable)
            .orElse(false);
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getItemUseTimeLeft()I",
            ordinal = 0
        )
    )
    private int useDifference(AbstractClientPlayerEntity instance, @Local(argsOnly = true) ItemStack stack, @Share("shooterItemComponent") LocalRef<ShooterItemComponent> shooterItemComponent) {
        return ShooterItemComponent.useDuration(stack) - instance.itematic$itemUsedTicks();
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/CrossbowItem;isCharged(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean isChargedUseItemComponent(ItemStack stack, @Share("shooterItemComponent") LocalRef<ShooterItemComponent> shooterItemComponent) {
        return shooterItemComponent.get().isCharged(stack);
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getMaxUseTime()I"
        )
    )
    private int getMaxUseTimeReturnZero(ItemStack stack, AbstractClientPlayerEntity player) {
        return 0;
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;getItemUseTimeLeft()I"
        )
    )
    private int getUseTimeLeftUseUsedTicks(ClientPlayerEntity instance) {
        return -instance.itematic$itemUsedTicks();
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/CrossbowItem;getPullTime(Lnet/minecraft/item/ItemStack;)I"
        )
    )
    private int getPullTimeUseItemComponent(ItemStack stack) {
        return ShooterItemComponent.getPullTime(stack);
    }

    @Redirect(
        method = { "getHandRenderType", "getUsingItemHandRenderType", "isChargedCrossbow" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CROSSBOW:Lnet/minecraft/item/Item;"
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
                target = "Lnet/minecraft/item/Items;FILLED_MAP:Lnet/minecraft/item/Item;"
            )
        )
    )
    private boolean isOfForFilledMapUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasComponent(ItemComponentTypes.MAP_HOLDER);
    }

    @Redirect(
        method = "applyEatOrDrinkTransformation",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getMaxUseTime()I"
        )
    )
    private int getMaxUseTimeUseCustomImplementation(ItemStack instance) {
        return instance.itematic$useDuration(this.client.player);
    }
}
