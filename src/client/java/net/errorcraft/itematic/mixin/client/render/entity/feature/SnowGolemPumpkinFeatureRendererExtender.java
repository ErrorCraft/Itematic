package net.errorcraft.itematic.mixin.client.render.entity.feature;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.render.entity.feature.SnowGolemPumpkinFeatureRenderer;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SnowGolemPumpkinFeatureRenderer.class)
public class SnowGolemPumpkinFeatureRendererExtender {
    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/SnowGolemEntity;FFFFFF)V",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForCarvedPumpkinUseCreateStack(ItemConvertible item, @Local(argsOnly = true) SnowGolemEntity snowGolemEntity) {
        return snowGolemEntity.getWorld().itematic$createStack(ItemKeys.CARVED_PUMPKIN);
    }
}
