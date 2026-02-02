package net.errorcraft.itematic.mixin.client.render.entity.feature;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.client.render.ItematicTexturedRenderLayers;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ArmorItemComponent;
import net.errorcraft.itematic.item.component.components.EquipmentItemComponent;
import net.errorcraft.itematic.item.component.components.TintedItemComponent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererExtender<S extends BipedEntityRenderState, M extends BipedEntityModel<S>, A extends BipedEntityModel<S>> {
    @Unique
    private SpriteAtlasTexture armorMaterialsAtlas;

    @Inject(
        method = "<init>(Lnet/minecraft/client/render/entity/feature/FeatureRendererContext;Lnet/minecraft/client/render/entity/model/BipedEntityModel;Lnet/minecraft/client/render/entity/model/BipedEntityModel;Lnet/minecraft/client/render/entity/model/BipedEntityModel;Lnet/minecraft/client/render/entity/model/BipedEntityModel;Lnet/minecraft/client/render/model/BakedModelManager;)V",
        at = @At("TAIL")
    )
    private void setArmorMaterialsAtlas(FeatureRendererContext<S, M> context, A innerModel, A outerModel, A babyInnerModel, A babyOuterModel, BakedModelManager bakedModelManager, CallbackInfo info) {
        this.armorMaterialsAtlas = bakedModelManager.getAtlas(ItematicTexturedRenderLayers.ARMOR_MATERIALS_ATLAS_TEXTURE);
    }

    @ModifyConstant(
        method = "renderArmor",
        constant = @Constant(
            classValue = ArmorItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfArmorItemUseItemComponentCheck(Object reference, Class<ArmorItem> clazz, @Local(argsOnly = true) ItemStack itemStack, @Share("equipmentItemComponent") LocalRef<EquipmentItemComponent> equipmentItemComponent) {
        Optional<EquipmentItemComponent> optionalComponent = itemStack.itematic$getComponent(ItemComponentTypes.EQUIPMENT);
        optionalComponent.ifPresent(equipmentItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "renderArmor",
        at = @At("LOAD"),
        ordinal = 0
    )
    private Item castToArmorItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ArmorItem;getSlotType()Lnet/minecraft/entity/EquipmentSlot;"
        )
    )
    private EquipmentSlot getSlotTypeUseItemComponent(ArmorItem instance, @Share("equipmentItemComponent") LocalRef<EquipmentItemComponent> equipmentItemComponent) {
        return equipmentItemComponent.get().slot();
    }

    @Inject(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V"
        ),
        cancellable = true
    )
    private void storeArmorItemComponent(MatrixStack matrices, VertexConsumerProvider vertexConsumers, S state, ItemStack stack, EquipmentSlot slot, int light, A armorModel, CallbackInfo info, @Share("armorItemComponent") LocalRef<ArmorItemComponent> armorItemComponent) {
        stack.itematic$getComponent(ItemComponentTypes.ARMOR)
            .ifPresentOrElse(armorItemComponent::set, info::cancel);
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ArmorItem;getMaterial()Lnet/minecraft/registry/entry/RegistryEntry;"
        )
    )
    private RegistryEntry<net.minecraft.item.ArmorMaterial> getMaterialReturnNull(ArmorItem instance) {
        return null;
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/entry/RegistryEntry;value()Ljava/lang/Object;"
        )
    )
    private <R> R registryEntryValueReturnNull(RegistryEntry<R> instance) {
        return null;
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
        )
    )
    private boolean isInForDyeableItemUseItemComponentCheck(ItemStack instance, TagKey<Item> tag, @Share("tintedItemComponent") LocalRef<TintedItemComponent> tintedItemComponent) {
        Optional<TintedItemComponent> optionalComponent = instance.itematic$getComponent(ItemComponentTypes.TINTED);
        optionalComponent.ifPresent(tintedItemComponent::set);
        return optionalComponent.isPresent();
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/DyedColorComponent;getColor(Lnet/minecraft/item/ItemStack;I)I"
        )
    )
    private int getColorUseItemComponent(ItemStack stack, int defaultColor, @Share("tintedItemComponent") LocalRef<TintedItemComponent> tintedItemComponent) {
        return tintedItemComponent.get().tint().color(stack, 0);
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ArmorMaterial;layers()Ljava/util/List;"
        )
    )
    private List<ArmorMaterial.Layer> layersForArmorMaterialReturnNull(ArmorMaterial instance) {
        return null;
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;iterator()Ljava/util/Iterator;"
        )
    )
    private <E> Iterator<String> iteratorForLayersUseCustomIterator(List<E> instance, @Share("tintedItemComponent") LocalRef<TintedItemComponent> tintedItemComponent) {
        if (tintedItemComponent.get() != null) {
            return Stream.of("", "_overlay").iterator();
        }
        return Stream.of("").iterator();
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Iterator;next()Ljava/lang/Object;"
        )
    )
    private <E> E nextElementForIteratorReturnNull(Iterator<E> instance) {
        return null;
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ArmorMaterial$Layer;getTexture(Z)Lnet/minecraft/util/Identifier;"
        )
    )
    private Identifier getTextureUseItemComponent(ArmorMaterial.Layer instance, boolean secondLayer, @Local Iterator<String> layers, @Share("armorItemComponent") LocalRef<ArmorItemComponent> armorItemComponent) {
        return armorItemComponent.get()
            .textureId(secondLayer)
            .withPath(path -> path + layers.next());
    }

    @Redirect(
        method = "renderArmor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ArmorMaterial$Layer;isDyeable()Z"
        )
    )
    private boolean isDyeableUseItemComponent(ArmorMaterial.Layer instance, @Share("tintedItemComponent") LocalRef<TintedItemComponent> tintedItemComponent) {
        return tintedItemComponent.get() != null;
    }

    @ModifyArg(
        method = "renderArmorParts",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"
        )
    )
    private RenderLayer useArmorMaterialRenderLayer(RenderLayer layer) {
        return ItematicTexturedRenderLayers.ARMOR_MATERIAL_RENDER_LAYER;
    }

    @ModifyExpressionValue(
        method = "renderArmorParts",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"
        )
    )
    private VertexConsumer useArmorMaterialsAtlas(VertexConsumer original, @Local(argsOnly = true) Identifier armorTexture) {
        return this.armorMaterialsAtlas.getSprite(armorTexture)
            .getTextureSpecificVertexConsumer(original);
    }
}
