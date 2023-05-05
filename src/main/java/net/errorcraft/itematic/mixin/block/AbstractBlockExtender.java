package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.item.ItemUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockExtender {
    @Shadow
    protected abstract Block asBlock();

    @Redirect(
        method = "canReplace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfUseItemKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemUtil.keyFromBlock(this.asBlock()));
    }
}
