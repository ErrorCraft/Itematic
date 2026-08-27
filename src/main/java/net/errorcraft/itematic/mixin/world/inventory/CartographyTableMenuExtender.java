package net.errorcraft.itematic.mixin.world.inventory;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(CartographyTableMenu.class)
public class CartographyTableMenuExtender {
    @Redirect(
        method = {
            "lambda$setupResultSlot$0",
            "quickMoveStack"
        },
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

    @Redirect(
        method = {
            "lambda$setupResultSlot$0",
            "quickMoveStack"
        },
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
        method = {
            "lambda$setupResultSlot$0",
            "quickMoveStack"
        },
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

    @Mixin(targets = "net/minecraft/world/inventory/CartographyTableMenu$4")
    public static class AdditionsSlotExtender {
        @Redirect(
            method = "mayPlace",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
                ordinal = 0
            )
        )
        private boolean isPaperCheckId(ItemStack instance, Object o) {
            return instance.is(ItemIds.PAPER);
        }

        @Redirect(
            method = "mayPlace",
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

        @Redirect(
            method = "mayPlace",
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
    }
}
