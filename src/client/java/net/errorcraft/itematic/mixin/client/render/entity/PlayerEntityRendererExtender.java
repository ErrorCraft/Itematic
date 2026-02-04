package net.errorcraft.itematic.mixin.client.render.entity;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodTypes;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererExtender {
    @Redirect(
        method = "updateHandState",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfForCrossbowUseItemComponent(ItemStack instance, Item item) {
        return instance.itematic$getBehavior(ItemComponentTypes.SHOOTER)
            .map(ShooterItemComponent::method)
            .filter(method -> method.type() == ShooterMethodTypes.CHARGEABLE)
            .isPresent();
    }
}
