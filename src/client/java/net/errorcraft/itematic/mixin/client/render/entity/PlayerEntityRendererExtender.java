package net.errorcraft.itematic.mixin.client.render.entity;

import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.ShooterItemBehavior;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AvatarRenderer.class)
public class PlayerEntityRendererExtender {
    @Redirect(
        method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private static boolean isOfForCrossbowUseItemBehavior(ItemStack instance, Item item) {
        return instance.itematic$getBehavior(ItemBehaviorType.SHOOTER)
            .map(ShooterItemBehavior::method)
            .filter(method -> method.type() == ShooterMethodType.CHARGEABLE)
            .isPresent();
    }

    @Redirect(
        method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private static boolean isOfForSpyglassUseItemBehaviorCheck(ItemStack instance, Item item) {
        return instance.itematic$hasBehavior(ItemBehaviorType.ZOOM);
    }
}
