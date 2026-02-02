package net.errorcraft.itematic.mixin.client.render.entity.feature;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.client.render.entity.feature.HorseArmorFeatureRendererAccess;
import net.errorcraft.itematic.client.render.ItematicTexturedRenderLayers;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ArmorItemComponent;
import net.errorcraft.itematic.item.component.components.TintedItemComponent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.feature.HorseArmorFeatureRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(HorseArmorFeatureRenderer.class)
public class HorseArmorFeatureRendererExtender implements HorseArmorFeatureRendererAccess {
    @Unique
    private SpriteAtlasTexture armorMaterialsAtlas;

    @ModifyConstant(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/HorseEntityRenderState;FF)V",
        constant = @Constant(
            classValue = AnimalArmorItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfAnimalArmorItemUseItemComponentCheck(Object reference, Class<AnimalArmorItem> clazz, @Local ItemStack itemStack, @Share("armorItemComponent") LocalRef<ArmorItemComponent> armorItemComponent) {
        Optional<ArmorItemComponent> optionalComponent = itemStack.itematic$getComponent(ItemComponentTypes.ARMOR);
        optionalComponent.ifPresent(armorItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/HorseEntityRenderState;FF)V",
        at = @At("LOAD")
    )
    private Item castToAnimalArmorItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/HorseEntityRenderState;FF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/AnimalArmorItem;getType()Lnet/minecraft/item/AnimalArmorItem$Type;"
        )
    )
    private AnimalArmorItem.Type getTypeUseItemComponent(AnimalArmorItem instance, @Share("armorItemComponent") LocalRef<ArmorItemComponent> armorItemComponent) {
        return armorItemComponent.get().armorType().orElse(null);
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/HorseEntityRenderState;FF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/AnimalArmorItem;getEntityTexture()Lnet/minecraft/util/Identifier;"
        )
    )
    private Identifier getEntityTextureReturnNull(AnimalArmorItem instance) {
        return null;
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/HorseEntityRenderState;FF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/RenderLayer;getEntityCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"
        )
    )
    private RenderLayer getEntityCutoutNoCullReturnNull(Identifier texture) {
        return null;
    }

    @ModifyArg(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/HorseEntityRenderState;FF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"
        )
    )
    private RenderLayer useArmorMaterialRenderLayer(RenderLayer layer) {
        return ItematicTexturedRenderLayers.ARMOR_MATERIAL_RENDER_LAYER;
    }

    @ModifyExpressionValue(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/HorseEntityRenderState;FF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"
        )
    )
    private VertexConsumer useArmorMaterialsAtlas(VertexConsumer original, @Share("armorItemComponent") LocalRef<ArmorItemComponent> armorItemComponent) {
        return this.armorMaterialsAtlas.getSprite(armorItemComponent.get().textureId())
            .getTextureSpecificVertexConsumer(original);
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/HorseEntityRenderState;FF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
        )
    )
    private boolean isInForDyeableItemUseItemComponentCheck(ItemStack instance, TagKey<Item> tag, @Local ItemStack itemStack, @Share("tintedItemComponent") LocalRef<TintedItemComponent> tintedItemComponent) {
        Optional<TintedItemComponent> optionalComponent = itemStack.itematic$getComponent(ItemComponentTypes.TINTED);
        optionalComponent.ifPresent(tintedItemComponent::set);
        return optionalComponent.isPresent();
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/HorseEntityRenderState;FF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/DyedColorComponent;getColor(Lnet/minecraft/item/ItemStack;I)I"
        )
    )
    private int getColorUseItemComponent(ItemStack stack, int defaultColor, @Share("tintedItemComponent") LocalRef<TintedItemComponent> tintedItemComponent) {
        return tintedItemComponent.get().tint().color(stack, 0);
    }

    @Override
    public void itematic$setArmorMaterialsAtlas(BakedModelManager bakery) {
        this.armorMaterialsAtlas = bakery.getAtlas(ItematicTexturedRenderLayers.ARMOR_MATERIALS_ATLAS_TEXTURE);
    }
}
