package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.CandleBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CandleBlock.class)
public abstract class CandleBlockExtender extends AbstractCandleBlock {
    protected CandleBlockExtender(Settings settings) {
        super(settings);
    }

    @Redirect(
        method = "canReplace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        )
    )
    private Item getItemReturnNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "canReplace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/CandleBlock;asItem()Lnet/minecraft/item/Item;"
        )
    )
    private Item asItemReturnNull(CandleBlock instance) {
        return null;
    }

    @ModifyExpressionValue(
        method = "canReplace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemPlacementContext;shouldCancelInteraction()Z"
        )
    )
    private boolean equalItemsUseRegistryKeyCheck(boolean original, @Local(argsOnly = true) ItemPlacementContext context) {
        return original || !context.getStack().itematic$isOf(this.itematic$asItemKey());
    }
}
