package net.errorcraft.itematic.mixin.client.renderer.entity;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractSkeletonRenderer.class)
public class AbstractSkeletonRendererExtender {
    @Redirect(
        method = "extractRenderState(Lnet/minecraft/world/entity/monster/skeleton/AbstractSkeleton;Lnet/minecraft/client/renderer/entity/state/SkeletonRenderState;F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBowCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.BOW);
    }
}
