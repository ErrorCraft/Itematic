package net.errorcraft.itematic.mixin.client.render.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HumanoidArmorLayer.class)
public class ArmorFeatureRendererExtender<S extends HumanoidRenderState> {
    @Inject(
        method = "shouldRender(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void checkPresenceEquipmentBehavior(ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> info) {
        if (!stack.itematic$hasBehavior(ItemBehaviorType.EQUIPMENT)) {
            info.setReturnValue(false);
        }
    }

    @Inject(
        method = "renderArmorPiece",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceEquipmentBehavior(PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, ItemStack stack, EquipmentSlot slot, int light, S bipedEntityRenderState, CallbackInfo info) {
        if (!stack.itematic$hasBehavior(ItemBehaviorType.EQUIPMENT)) {
            info.cancel();
        }
    }
}
