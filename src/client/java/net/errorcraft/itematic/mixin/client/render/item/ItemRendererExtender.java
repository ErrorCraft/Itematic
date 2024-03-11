package net.errorcraft.itematic.mixin.client.render.item;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.TintedItemComponent;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DefaultedRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Collections;
import java.util.Iterator;

@Mixin(ItemRenderer.class)
public class ItemRendererExtender {
    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;iterator()Ljava/util/Iterator;"
        )
    )
    private Iterator<Item> doNotRegisterItems(DefaultedRegistry<Item> instance) {
        return Collections.emptyIterator();
    }

    @Redirect(
        method = { "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", "getModel" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;TRIDENT:Lnet/minecraft/item/Item;"
            )
        )
    )
    private boolean isOfForTridentUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.TRIDENT);
    }

    @Redirect(
        method = { "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", "getModel" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;SPYGLASS:Lnet/minecraft/item/Item;"
            )
        )
    )
    private boolean isOfForSpyglassUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SPYGLASS);
    }

    @Redirect(
        method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/render/model/BakedModel;isBuiltin()Z"
            )
        )
    )
    private boolean isOfForTridentBuiltinCheckUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.TRIDENT);
    }

    @Redirect(
        method = "usesDynamicDisplay",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfForClockUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.CLOCK);
    }

    @Redirect(
        method = "renderBakedItemQuads",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/color/item/ItemColors;getColor(Lnet/minecraft/item/ItemStack;I)I"
        )
    )
    private int getColorUseItemComponent(ItemColors instance, ItemStack item, int tintIndex) {
        return item.itematic$getComponent(ItemComponentTypes.TINTED)
            .map(TintedItemComponent::tint)
            .map(c -> c.color(item, tintIndex))
            .orElse(ItemColor.DEFAULT_COLOR);
    }
}
