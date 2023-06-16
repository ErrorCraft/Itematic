package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.WitherSkullBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WitherSkullBlock.class)
public class WitherSkullBlockExtender {
    @Redirect(
        method = "canDispense",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean canDispenseIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.WITHER_SKELETON_SKULL);
    }
}
