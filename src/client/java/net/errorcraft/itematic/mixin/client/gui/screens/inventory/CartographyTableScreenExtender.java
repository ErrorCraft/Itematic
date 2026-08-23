package net.errorcraft.itematic.mixin.client.gui.screens.inventory;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(CartographyTableScreen.class)
public class CartographyTableScreenExtender {
    @Redirect(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;PAPER:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isPaperCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.PAPER);
    }

    @Redirect(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;MAP:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isMapCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.MAP);
    }

    @Redirect(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;GLASS_PANE:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isGlassPaneCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.GLASS_PANE);
    }
}
