package net.errorcraft.itematic.mixin.client.render.entity.feature;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererExtender<S extends BipedEntityRenderState, M extends BipedEntityModel<S>, A extends BipedEntityModel<S>> {
    @Inject(
        method = "hasModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void checkPresenceEquipmentBehavior(ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> info) {
        if (!stack.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            info.setReturnValue(false);
        }
    }

    @Inject(
        method = "renderArmor",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceEquipmentBehavior(MatrixStack matrices, VertexConsumerProvider vertexConsumers, S state, ItemStack stack, EquipmentSlot slot, int light, A armorModel, CallbackInfo info) {
        if (!stack.itematic$hasBehavior(ItemComponentTypes.EQUIPMENT)) {
            info.cancel();
        }
    }
}
