package net.errorcraft.itematic.mixin.client.render.entity.feature;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BlockItemComponent;
import net.minecraft.block.Block;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(HeadFeatureRenderer.class)
public class HeadFeatureRendererExtender {
    @ModifyConstant(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/LivingEntityRenderState;FF)V",
        constant = @Constant(
            classValue = BlockItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBlockItemUseItemComponentCheck(Object reference, Class<BlockItem> clazz, @Local ItemStack itemStack, @Share("blockItemComponent") LocalRef<BlockItemComponent> blockItemComponent) {
        Optional<BlockItemComponent> optionalComponent = itemStack.itematic$getBehavior(ItemComponentTypes.BLOCK);
        optionalComponent.ifPresent(blockItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/LivingEntityRenderState;FF)V",
        at = @At(
            value = "LOAD",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/render/entity/feature/HeadFeatureRenderer;getContextModel()Lnet/minecraft/client/render/entity/model/EntityModel;"
            )
        )
    )
    private Item castToBlockItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/LivingEntityRenderState;FF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/BlockItem;getBlock()Lnet/minecraft/block/Block;"
        )
    )
    private Block getBlockUseItemComponent(BlockItem instance, @Share("blockItemComponent") LocalRef<BlockItemComponent> blockItemComponent) {
        return blockItemComponent.get().block().defaultBlock().value();
    }
}
