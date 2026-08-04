package net.errorcraft.itematic.mixin.client.render.item;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodTypes;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Optional;
import java.util.OptionalInt;

@Mixin(ItemInHandRenderer.class)
public class HeldItemRendererExtender {
    @Redirect(
        method = {
            "evaluateWhichHandsToRender",
            "selectionUsingItemWhileHoldingBowLike"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;BOW:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            ),
            to = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;CROSSBOW:Lnet/minecraft/world/item/Item;",
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
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;CROSSBOW:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForCrossbowUseItemComponent(ItemStack instance, Item item, AbstractClientPlayer player, @Share("useDuration") LocalIntRef useDuration) {
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
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/AbstractClientPlayer;getUseItemRemainingTicks()I",
            ordinal = 0
        )
    )
    private int useDifferenceForCrossbow(AbstractClientPlayer instance, @Share("useDuration") LocalIntRef useDuration) {
        return useDuration.get() - instance.itematic$itemUsedTicks();
    }

    @Redirect(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getUseDuration(Lnet/minecraft/world/entity/LivingEntity;)I"
        )
    )
    private int getMaxUseTimeReturnZero(ItemStack instance, LivingEntity livingEntity) {
        return 0;
    }

    @Redirect(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/AbstractClientPlayer;getUseItemRemainingTicks()I",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/player/AbstractClientPlayer;getUsedItemHand()Lnet/minecraft/world/InteractionHand;",
                ordinal = 0
            )
        )
    )
    private int getUseTimeLeftForCrossbowUseNegatedUsedTicks(AbstractClientPlayer instance) {
        return -instance.itematic$itemUsedTicks();
    }

    @Redirect(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/AbstractClientPlayer;getUseItemRemainingTicks()I",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
                ordinal = 0
            )
        )
    )
    private int getUseTimeLeftForUseAnimationCheckUseUsedTicks(AbstractClientPlayer instance) {
        return instance.itematic$itemUsedTicks();
    }

    @Redirect(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/AbstractClientPlayer;getUseItemRemainingTicks()I"
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyEatTransform(Lcom/mojang/blaze3d/vertex/PoseStack;FLnet/minecraft/world/entity/HumanoidArm;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)V"
            )
        )
    )
    private int getUseTimeLeftForBowAndSpearUseNegatedUsedTicks(AbstractClientPlayer instance) {
        return -instance.itematic$itemUsedTicks();
    }

    @Redirect(
        method = {
            "evaluateWhichHandsToRender",
            "selectionUsingItemWhileHoldingBowLike",
            "isChargedCrossbow"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;CROSSBOW:Lnet/minecraft/world/item/Item;",
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
}
