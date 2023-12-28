package net.errorcraft.itematic.mixin.client.render.entity.feature;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.client.render.entity.feature.HorseArmorFeatureRendererAccess;
import net.errorcraft.itematic.client.render.ItematicTexturedRenderLayers;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.AnimalArmorItemComponent;
import net.errorcraft.itematic.item.component.components.TintedItemComponent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HorseArmorFeatureRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.DyeableAnimalArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        constant = @Constant(
            classValue = AnimalArmorItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfAnimalArmorItemUseItemComponentCheck(Object reference, Class<AnimalArmorItem> clazz, @Local ItemStack itemStack, @Share("animalArmorItemComponent") LocalRef<AnimalArmorItemComponent> animalArmorItemComponent) {
        Optional<AnimalArmorItemComponent> optionalComponent = itemStack.itematic$getComponent(ItemComponentTypes.ANIMAL_ARMOR);
        optionalComponent.ifPresent(animalArmorItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        at = @At("LOAD")
    )
    private Item castToAnimalArmorItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/AnimalArmorItem;getType()Lnet/minecraft/item/AnimalArmorItem$Type;"
        )
    )
    private AnimalArmorItem.Type getTypeUseItemComponent(AnimalArmorItem instance, @Share("animalArmorItemComponent") LocalRef<AnimalArmorItemComponent> animalArmorItemComponent) {
        return animalArmorItemComponent.get().armorType();
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/AnimalArmorItem;getEntityTexture()Lnet/minecraft/util/Identifier;"
        )
    )
    private Identifier getEntityTextureReturnNull(AnimalArmorItem instance) {
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
    private VertexConsumer getBufferUseItemComponent(VertexConsumerProvider instance, RenderLayer renderLayer, @Share("animalArmorItemComponent") LocalRef<AnimalArmorItemComponent> animalArmorItemComponent) {
        Sprite sprite = this.armorMaterialsAtlas.getSprite(animalArmorItemComponent.get().textureId());
        return sprite.getTextureSpecificVertexConsumer(instance.getBuffer(ItematicTexturedRenderLayers.ARMOR_MATERIAL_RENDER_LAYER));
    }

    @ModifyConstant(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        constant = @Constant(
            classValue = DyeableAnimalArmorItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfDyeableAnimalArmorItemItemUseItemComponentCheck(Object reference, Class<DyeableAnimalArmorItem> clazz, @Local ItemStack itemStack, @Share("tintedItemComponent") LocalRef<TintedItemComponent> tintedItemComponent) {
        Optional<TintedItemComponent> optionalComponent = itemStack.itematic$getComponent(ItemComponentTypes.TINTED);
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
    private AnimalArmorItem castToDyeableAnimalArmorItemUseNull(AnimalArmorItem instance) {
        return null;
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/DyeableAnimalArmorItem;getColor(Lnet/minecraft/item/ItemStack;)I"
        )
    )
    private int getColorUseItemComponent(DyeableAnimalArmorItem instance, ItemStack stack, @Share("tintedItemComponent") LocalRef<TintedItemComponent> tintedItemComponent) {
        return tintedItemComponent.get().tint().getColor(stack, 0);
    }

    @Override
    public void itematic$setArmorMaterialsAtlas(BakedModelManager bakery) {
        this.armorMaterialsAtlas = bakery.getAtlas(ItematicTexturedRenderLayers.ARMOR_MATERIALS_ATLAS_TEXTURE);
    }
}
