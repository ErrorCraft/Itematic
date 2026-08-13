package net.errorcraft.itematic.mixin.client.render.entity;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.client.renderer.entity.WitchRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WitchRenderer.class)
public class WitchEntityRendererExtender {
    @Redirect(
        method = "extractRenderState(Lnet/minecraft/world/entity/monster/Witch;Lnet/minecraft/client/renderer/entity/state/WitchRenderState;F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForPotionUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.POTION);
    }
}
