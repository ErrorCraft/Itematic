package net.errorcraft.itematic.mixin.client.render.item;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BlockItemComponent;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererExtender {
    @ModifyConstant(
        method = "render",
        constant = @Constant(
            classValue = BlockItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfBlockItemUseItemComponentCheck(Object reference, Class<BlockItem> clazz, @Local ItemStack itemStack, @Share("blockItemComponent") LocalRef<BlockItemComponent> blockItemComponent) {
        Optional<BlockItemComponent> optionalComponent = itemStack.getComponent(ItemComponentTypes.BLOCK);
        optionalComponent.ifPresent(blockItemComponent::set);
        return optionalComponent.isPresent();
    }

    @ModifyVariable(
        method = "render",
        at = @At("LOAD"),
        slice = @Slice(
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/BlockItem;getBlock()Lnet/minecraft/block/Block;"
            )
        )
    )
    private Item castToBlockItemUseNull(Item instance) {
        return null;
    }

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/BlockItem;getBlock()Lnet/minecraft/block/Block;"
        )
    )
    private Block getBlockUseItemComponent(BlockItem instance, @Share("blockItemComponent") LocalRef<BlockItemComponent> blockItemComponent) {
        return blockItemComponent.get().block().value();
    }

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/ShulkerBoxBlock;getColor(Lnet/minecraft/item/Item;)Lnet/minecraft/util/DyeColor;"
        )
    )
    private DyeColor getColorUseInstanceMethod(Item item, @Local Block block) {
        return ((ShulkerBoxBlock) block).getColor();
    }

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfForShieldUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.SHIELD);
    }
}
