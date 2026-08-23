package net.errorcraft.itematic.mixin.client.renderer.entity;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.client.renderer.entity.WitchRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WitchRenderer.class)
public class WitchRendererExtender {
    @Redirect(
        method = "extractRenderState(Lnet/minecraft/world/entity/monster/Witch;Lnet/minecraft/client/renderer/entity/state/WitchRenderState;F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isPotionCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.POTION);
    }
}
