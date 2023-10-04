package net.errorcraft.itematic.mixin.client.render.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.HorseEntityRenderer;
import net.minecraft.client.render.entity.feature.HorseArmorFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HorseEntityRenderer.class)
public class HorseEntityRendererExtender {
    @ModifyExpressionValue(
        method = "<init>",
        at = @At(
            value = "NEW",
            target = "net/minecraft/client/render/entity/feature/HorseArmorFeatureRenderer"
        )
    )
    private HorseArmorFeatureRenderer setArmorMaterialsAtlas(HorseArmorFeatureRenderer original, @Local EntityRendererFactory.Context context) {
        original.setArmorMaterialsAtlas(context.getModelManager());
        return original;
    }
}
