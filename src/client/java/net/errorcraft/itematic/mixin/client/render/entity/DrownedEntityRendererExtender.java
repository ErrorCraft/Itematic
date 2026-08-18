package net.errorcraft.itematic.mixin.client.render.entity;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.client.renderer.entity.DrownedRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DrownedRenderer.class)
public class DrownedEntityRendererExtender {
    @Redirect(
        method = "getArmPose(Lnet/minecraft/world/entity/monster/zombie/Drowned;Lnet/minecraft/world/entity/HumanoidArm;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForTridentUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.TRIDENT);
    }
}
