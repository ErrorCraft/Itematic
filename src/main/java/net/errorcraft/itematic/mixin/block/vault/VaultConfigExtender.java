package net.errorcraft.itematic.mixin.block.vault;

import net.minecraft.block.vault.VaultConfig;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VaultConfig.class)
public class VaultConfigExtender {
    @Redirect(
        method = "<init>()V",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackReturnEmptyStack(ItemConvertible item) {
        return ItemStack.EMPTY;
    }
}
