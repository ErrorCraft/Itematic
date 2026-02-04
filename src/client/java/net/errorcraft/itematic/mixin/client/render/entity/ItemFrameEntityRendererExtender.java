package net.errorcraft.itematic.mixin.client.render.entity;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemFrameEntityRenderer.class)
public class ItemFrameEntityRendererExtender {
    @Redirect(
        method = "getModelId",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForFilledMapUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasBehavior(ItemComponentTypes.MAP_HOLDER);
    }
}
