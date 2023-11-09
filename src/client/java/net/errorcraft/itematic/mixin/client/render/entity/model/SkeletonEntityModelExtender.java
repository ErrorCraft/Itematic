package net.errorcraft.itematic.mixin.client.render.entity.model;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkeletonEntityModel.class)
public class SkeletonEntityModelExtender {
    @Redirect(
        method = { "animateModel(Lnet/minecraft/entity/mob/MobEntity;FFF)V", "setAngles(Lnet/minecraft/entity/mob/MobEntity;FFFFF)V" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BOW);
    }
}
