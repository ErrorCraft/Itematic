package net.errorcraft.itematic.mixin.client.render.item;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodTypes;
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

import java.util.Optional;
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
    private static boolean isOfForBowUseItemComponent(ItemStack instance, Item item) {
        return instance.itematic$getBehavior(ItemComponentTypes.SHOOTER)
            .map(ShooterItemComponent::method)
            .filter(method -> method.type() == ShooterMethodTypes.DIRECT)
            .isPresent();
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
        Optional<ShooterItemComponent> optionalShooter = instance.itematic$getBehavior(ItemComponentTypes.SHOOTER);
        if (optionalShooter.isEmpty()) {
            return false;
        }

        if (optionalShooter.get().method().type() !=  ShooterMethodTypes.CHARGEABLE) {
            return false;
        }

        OptionalInt optionalUseDuration = optionalShooter.get().useDuration(instance, player);
        if (optionalUseDuration.orElse(0) <= 0) {
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
    private int useDifferenceForCrossbow(AbstractClientPlayerEntity instance, @Share("useDuration") LocalIntRef useDuration) {
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
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getItemUseTimeLeft()I",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                ordinal = 0
            )
        )
    )
    private int getUseTimeLeftForCrossbowUseNegatedUsedTicks(AbstractClientPlayerEntity instance) {
        return -instance.itematic$itemUsedTicks();
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getItemUseTimeLeft()I",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
                ordinal = 0
            )
        )
    )
    private int getUseTimeLeftForUseAnimationCheckUseUsedTicks(AbstractClientPlayerEntity instance) {
        return instance.itematic$itemUsedTicks();
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getItemUseTimeLeft()I"
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEatOrDrinkTransformation(Lnet/minecraft/client/util/math/MatrixStack;FLnet/minecraft/util/Arm;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;)V"
            )
        )
    )
    private int getUseTimeLeftForBowAndSpearUseNegatedUsedTicks(AbstractClientPlayerEntity instance) {
        return -instance.itematic$itemUsedTicks();
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
    private static boolean isOfForCrossbowUseItemComponentStatic(ItemStack instance, Item item) {
        return instance.itematic$getBehavior(ItemComponentTypes.SHOOTER)
            .map(ShooterItemComponent::method)
            .filter(method -> method.type() == ShooterMethodTypes.CHARGEABLE)
            .isPresent();
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
        return instance.itematic$hasBehavior(ItemComponentTypes.MAP_HOLDER);
    }
}
