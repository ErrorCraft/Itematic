package net.errorcraft.itematic.mixin.client.render.entity.feature;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.client.render.entity.feature.HorseArmorFeatureRendererAccess;
import net.errorcraft.itematic.client.render.ItematicTexturedRenderLayers;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.HorseArmorItemComponent;
import net.errorcraft.itematic.item.component.components.TintedItemComponent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HorseArmorFeatureRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.item.DyeableHorseArmorItem;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(HorseArmorFeatureRenderer.class)
public class HorseArmorFeatureRendererExtender implements HorseArmorFeatureRendererAccess {
    private SpriteAtlasTexture armorMaterialsAtlas;

    @ModifyConstant(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        constant = @Constant(
            classValue = HorseArmorItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfHorseArmorItemUseItemComponentCheck(Object reference, Class<HorseArmorItem> clazz, @Local ItemStack itemStack, @Share("horseArmorItemComponent") LocalRef<HorseArmorItemComponent> horseArmorItemComponent) {
        Optional<HorseArmorItemComponent> optionalComponent = itemStack.getComponent(ItemComponentTypes.HORSE_ARMOR);
        optionalComponent.ifPresent(horseArmorItemComponent::set);
        return optionalComponent.isPresent();
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;",
            ordinal = 1
        )
    )
    private Item castToHorseArmorItemUseNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/HorseArmorItem;getEntityTexture()Lnet/minecraft/util/Identifier;"
        )
    )
    private Identifier getEntityTextureReturnNull(HorseArmorItem instance) {
        return null;
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/RenderLayer;getEntityCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"
        )
    )
    private RenderLayer getEntityCutoutNoCullReturnNull(Identifier texture) {
        return null;
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"
        )
    )
    private VertexConsumer getBufferUseItemComponent(VertexConsumerProvider instance, RenderLayer renderLayer, @Share("horseArmorItemComponent") LocalRef<HorseArmorItemComponent> horseArmorItemComponent) {
        Sprite sprite = this.armorMaterialsAtlas.getSprite(horseArmorItemComponent.get().material().value().getHorseTextureId());
        return sprite.getTextureSpecificVertexConsumer(instance.getBuffer(ItematicTexturedRenderLayers.ARMOR_MATERIAL_RENDER_LAYER));
    }

    @ModifyConstant(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        constant = @Constant(
            classValue = DyeableHorseArmorItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfDyeableHorseArmorItemItemUseItemComponentCheck(Object reference, Class<DyeableHorseArmorItem> clazz, @Local ItemStack itemStack, @Share("tintedItemComponent") LocalRef<TintedItemComponent> tintedItemComponent) {
        Optional<TintedItemComponent> optionalComponent = itemStack.getComponent(ItemComponentTypes.TINTED);
        optionalComponent.ifPresent(tintedItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        at = @At("LOAD"),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/render/entity/model/HorseEntityModel;setAngles(Lnet/minecraft/entity/passive/AbstractHorseEntity;FFFFF)V"
            )
        ),
        ordinal = 0
    )
    private HorseArmorItem castToDyeableHorseArmorItemUseNull(HorseArmorItem instance) {
        return null;
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/DyeableHorseArmorItem;getColor(Lnet/minecraft/item/ItemStack;)I"
        )
    )
    private int getColorUseItemComponent(DyeableHorseArmorItem instance, ItemStack stack, @Share("tintedItemComponent") LocalRef<TintedItemComponent> tintedItemComponent) {
        return tintedItemComponent.get().tint().getColor(stack, 0);
    }

    @Override
    public void setArmorMaterialsAtlas(BakedModelManager bakery) {
        this.armorMaterialsAtlas = bakery.getAtlas(ItematicTexturedRenderLayers.ARMOR_MATERIALS_ATLAS_TEXTURE);
    }
}
