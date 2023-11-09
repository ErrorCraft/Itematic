package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.TntBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(TntBlock.class)
public class TntBlockExtender {
    @Redirect(
        method = "onUse",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/entity/player/PlayerEntity;isCreative()Z"
            )
        )
    )
    private boolean isOfForFlintAndSteelUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.FLINT_AND_STEEL);
    }
}
