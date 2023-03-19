package errorcraft.itematic.mixin.client.render.entity.feature;

import errorcraft.itematic.client.render.TexturedRenderLayersUtil;
import errorcraft.itematic.item.armor.ArmorMaterial;
import errorcraft.itematic.item.component.ItemComponentTypes;
import errorcraft.itematic.item.component.components.ArmorItemComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererExtender<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    @Shadow
    protected abstract void setVisible(A bipedModel, EquipmentSlot slot);

    @Shadow
    protected abstract boolean usesInnerModel(EquipmentSlot slot);

    public ArmorFeatureRendererExtender(FeatureRendererContext<T, M> context) {
        super(context);
    }

    private SpriteAtlasTexture armorMaterialsAtlas;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setArmorMaterialsAtlas(FeatureRendererContext<T, M> context, A innerModel, A outerModel, BakedModelManager bakery, CallbackInfo info) {
        this.armorMaterialsAtlas = bakery.getAtlas(TexturedRenderLayersUtil.ARMOR_MATERIALS_ATLAS_TEXTURE);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items. Should be changed eventually for armor trim support.
     */
    @Overwrite
    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model) {
        ItemStack stack = entity.getEquippedStack(armorSlot);
        Optional<ArmorItemComponent> optional = stack.getComponent(ItemComponentTypes.ARMOR);
        if (optional.isEmpty()) {
            return;
        }
        this.getContextModel().copyBipedStateTo(model);
        this.setVisible(model, armorSlot);
        boolean usesInnerModel = this.usesInnerModel(armorSlot);
        boolean hasGlint = stack.hasGlint();
        this.renderArmorParts(matrices, vertexConsumers, light, optional.get(), hasGlint, model, usesInnerModel, 1.0f, 1.0f, 1.0f, null);
    }

    private void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItemComponent component, boolean glint, A model, boolean secondTextureLayer, float red, float green, float blue, @Nullable String overlay) {
        Sprite sprite = this.armorMaterialsAtlas.getSprite(getArmorTexture(component, secondTextureLayer));
        VertexConsumer vertexConsumer = sprite.getTextureSpecificVertexConsumer(ItemRenderer.getArmorGlintConsumer(vertexConsumers, TexturedRenderLayersUtil.ARMOR_TRIMS_RENDER_LAYER, false, glint));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0f);
    }

    private static Identifier getArmorTexture(ArmorItemComponent component, boolean secondLayer) {
        ArmorMaterial armorMaterial = component.material().value();
        if (secondLayer) {
            return armorMaterial.getLeggingsTextureId();
        }
        return armorMaterial.getTextureId();
    }
}
