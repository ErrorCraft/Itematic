package net.errorcraft.itematic.mixin.client.render.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.WolfEntityRenderer;
import net.minecraft.client.render.entity.feature.WolfArmorFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WolfEntityRenderer.class)
public class WolfEntityRendererExtender {
    @ModifyExpressionValue(
        method = "<init>",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/client/render/entity/feature/FeatureRendererContext;Lnet/minecraft/client/render/entity/model/EntityModelLoader;)Lnet/minecraft/client/render/entity/feature/WolfArmorFeatureRenderer;"
        )
    )
    private WolfArmorFeatureRenderer setArmorMaterialsAtlas(WolfArmorFeatureRenderer original, @Local(argsOnly = true) EntityRendererFactory.Context context) {
        original.itematic$setArmorMaterialsAtlas(context.getModelManager());
        return original;
    }
}
