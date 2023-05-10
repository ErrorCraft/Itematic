package net.errorcraft.itematic.mixin.client.render.entity.feature;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EquipmentItemComponent;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Optional;

@Mixin(HeadFeatureRenderer.class)
public class HeadFeatureRendererExtender {
    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
        at = @At(
            value = "CONSTANT",
            args = "classValue=net/minecraft/item/ArmorItem",
            ordinal = 0
        )
    )
    private boolean renderInstanceOfArmorItemUseItemComponentCheck(Object reference, Class<ArmorItem> clazz, @Local ItemStack itemStack, @Share("equipmentItemComponent") LocalRef<EquipmentItemComponent> equipmentItemComponent) {
        Optional<EquipmentItemComponent> optionalComopnent = itemStack.getComponent(ItemComponentTypes.EQUIPMENT);
        optionalComopnent.ifPresent(equipmentItemComponent::set);
        return optionalComopnent.isPresent();
    }

    @ModifyVariable(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
        at = @At("LOAD"),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/render/block/entity/SkullBlockEntityRenderer;renderSkull(Lnet/minecraft/util/math/Direction;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/block/entity/SkullBlockEntityModel;Lnet/minecraft/client/render/RenderLayer;)V"
            )
        )
    )
    private Item renderCastToArmorItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ArmorItem;getSlotType()Lnet/minecraft/entity/EquipmentSlot;"
        )
    )
    private EquipmentSlot renderGetSlotTypeUseItemComponent(ArmorItem instance, @Share("equipmentItemComponent") LocalRef<EquipmentItemComponent> equipmentItemComponent) {
        return equipmentItemComponent.get().slot();
    }
}
