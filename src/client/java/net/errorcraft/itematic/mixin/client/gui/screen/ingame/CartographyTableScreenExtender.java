package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.gui.screen.ingame.CartographyTableScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(CartographyTableScreen.class)
public class CartographyTableScreenExtender {
    @Redirect(
        method = "drawBackground",
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
    private boolean drawBackgroundIsOfForPaperUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.PAPER);
    }
}
