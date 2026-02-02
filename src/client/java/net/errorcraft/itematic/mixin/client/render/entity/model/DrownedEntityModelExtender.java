package net.errorcraft.itematic.mixin.client.render.entity.model;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.render.entity.model.DrownedEntityModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DrownedEntityModel.class)
public class DrownedEntityModelExtender {
    @Redirect(
        method = "getArmPose(Lnet/minecraft/client/render/entity/state/ZombieEntityRenderState;Lnet/minecraft/util/Arm;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForTridentUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.TRIDENT);
    }
}
