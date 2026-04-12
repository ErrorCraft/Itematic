package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractBlockAccess;
import net.minecraft.block.Segmented;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Segmented.class)
public interface SegmentedExtender extends AbstractBlockAccess {
    @Redirect(
        method = "shouldAddSegment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(this.itematic$asItemKey());
    }
}
