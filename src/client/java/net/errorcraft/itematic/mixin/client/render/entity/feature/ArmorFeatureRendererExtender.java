package net.errorcraft.itematic.mixin.client.render.entity.feature;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.client.render.ItematicTexturedRenderLayers;
import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ArmorItemComponent;
import net.errorcraft.itematic.item.component.components.EquipmentItemComponent;
import net.errorcraft.itematic.item.component.components.TintedItemComponent;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererExtender<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
    private SpriteAtlasTexture armorMaterialsAtlas;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setArmorMaterialsAtlas(FeatureRendererContext<T, M> context, A innerModel, A outerModel, BakedModelManager bakery, CallbackInfo info) {
        this.armorMaterialsAtlas = bakery.getAtlas(ItematicTexturedRenderLayers.ARMOR_MATERIALS_ATLAS_TEXTURE);
    }

    @ModifyConstant(
        method = "renderArmor",
        constant = @Constant(
            classValue = ArmorItem.class,
            ordinal = 0
        )
    )
    private boolean renderArmorInstanceOfArmorItemUseItemComponentCheck(Object reference, Class<ArmorItem> clazz, @Local ItemStack itemStack, @Share("equipmentItemComponent") LocalRef<EquipmentItemComponent> equipmentItemComponent) {
        Optional<EquipmentItemComponent> optionalComponent = itemStack.getComponent(ItemComponentTypes.EQUIPMENT);
        optionalComponent.ifPresent(equipmentItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "renderArmor",
        at = @At("LOAD"),
        ordinal = 0
    )
    private Item renderArmorCastToArmorItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ArmorItem;getSlotType()Lnet/minecraft/entity/EquipmentSlot;"
        )
    )
    private EquipmentSlot renderArmorGetSlotTypeUseItemComponent(ArmorItem instance, @Share("equipmentItemComponent") LocalRef<EquipmentItemComponent> equipmentItemComponent) {
        return equipmentItemComponent.get().slot();
    }

    @Inject(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;getContextModel()Lnet/minecraft/client/render/entity/model/EntityModel;"
        ),
        cancellable = true
    )
    private void renderArmorStoreArmorItemComponent(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo info, @Local ItemStack itemStack, @Share("armorItemComponent") LocalRef<ArmorItemComponent> armorItemComponent) {
        Optional<ArmorItemComponent> optionalComponent = itemStack.getComponent(ItemComponentTypes.ARMOR);
        if (optionalComponent.isEmpty()) {
            info.cancel();
            return;
        }
        armorItemComponent.set(optionalComponent.get());
    }

    @ModifyConstant(
        method = "renderArmor",
        constant = @Constant(
            classValue = DyeableArmorItem.class,
            ordinal = 0
        )
    )
    private boolean renderArmorInstanceOfDyeableArmorItemUseItemComponentCheck(Object reference, Class<DyeableArmorItem> clazz, @Local ItemStack itemStack, @Share("tintedItemComponent") LocalRef<TintedItemComponent> tintedItemComponent) {
        Optional<TintedItemComponent> optionalComponent = itemStack.getComponent(ItemComponentTypes.TINTED);
        optionalComponent.ifPresent(tintedItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "renderArmor",
        at = @At("LOAD"),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;usesInnerModel(Lnet/minecraft/entity/EquipmentSlot;)Z"
            )
        ),
        ordinal = 0
    )
    private ArmorItem renderArmorCastToDyeableArmorItemUseNull(ArmorItem instance) {
        return null;
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/DyeableArmorItem;getColor(Lnet/minecraft/item/ItemStack;)I"
        )
    )
    private int renderArmorGetColorUseItemComponent(DyeableArmorItem instance, ItemStack stack, @Share("tintedItemComponent") LocalRef<TintedItemComponent> tintedItemComponent) {
        return tintedItemComponent.get().tint().getColor(stack, 0);
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/ArmorItem;Lnet/minecraft/client/render/entity/model/BipedEntityModel;ZFFFLjava/lang/String;)V"
        )
    )
    private void renderArmorRenderArmorUseItemComponent(ArmorFeatureRenderer<T, M, A> instance, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, A model, boolean secondTextureLayer, float red, float green, float blue, @Nullable String overlay, @Share("armorItemComponent") LocalRef<ArmorItemComponent> armorItemComponent) {
        this.renderArmorParts(matrices, vertexConsumers, light, armorItemComponent.get(), model, secondTextureLayer, red, green, blue, overlay);
    }

    private void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItemComponent component, A model, boolean secondTextureLayer, float red, float green, float blue, @Nullable String overlay) {
        Sprite sprite = this.armorMaterialsAtlas.getSprite(getArmorTexture(component, secondTextureLayer, overlay));
        VertexConsumer vertexConsumer = sprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(ItematicTexturedRenderLayers.ARMOR_MATERIAL_RENDER_LAYER));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0f);
    }

    private static Identifier getArmorTexture(ArmorItemComponent component, boolean secondLayer, @Nullable String overlay) {
        Identifier armorTexture = getArmorTexture(component.material().value(), secondLayer);
        if (overlay != null) {
            return armorTexture.withPath(path -> path + "_" + overlay);
        }
        return armorTexture;
    }

    private static Identifier getArmorTexture(ArmorMaterial armorMaterial, boolean secondLayer) {
        if (secondLayer) {
            return armorMaterial.getLeggingsTextureId();
        }
        return armorMaterial.getTextureId();
    }
}
