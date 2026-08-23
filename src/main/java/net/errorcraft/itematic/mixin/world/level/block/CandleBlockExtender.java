package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.CandleBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CandleBlock.class)
public abstract class CandleBlockExtender extends AbstractCandleBlock {
    protected CandleBlockExtender(Properties settings) {
        super(settings);
    }

    @Redirect(
        method = "canBeReplaced(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/BlockPlaceContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"
        )
    )
    @Nullable
    private Item getItemReturnNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "canBeReplaced(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/BlockPlaceContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/CandleBlock;asItem()Lnet/minecraft/world/item/Item;"
        )
    )
    @Nullable
    private Item asItemReturnNull(CandleBlock instance) {
        return null;
    }

    @ModifyExpressionValue(
        method = "canBeReplaced(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/BlockPlaceContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/context/BlockPlaceContext;isSecondaryUseActive()Z"
        )
    )
    private boolean equalItemsCheckId(boolean original, @Local(name = "context", argsOnly = true) BlockPlaceContext context) {
        return original || !context.getItemInHand().is(this.itematic$asItemId());
    }
}
