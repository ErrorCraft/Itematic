package net.errorcraft.itematic.mixin.screen;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CartographyTableScreenHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(CartographyTableScreenHandler.class)
public class CartographyTableScreenHandlerExtender {
    @Redirect(
        method = "method_17382",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        remap = false
    )
    private boolean method_17382IsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.PAPER);
    }

    @Redirect(
        method = "quickMove",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;PAPER:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean quickMoveIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.PAPER);
    }

    @Mixin(targets = "net/minecraft/screen/CartographyTableScreenHandler$4")
    public static class AdditionSlotExtender {
        @Redirect(
            method = "canInsert",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
                ordinal = 0
            )
        )
        private boolean canInsertIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.isOf(ItemKeys.PAPER);
        }
    }
}
