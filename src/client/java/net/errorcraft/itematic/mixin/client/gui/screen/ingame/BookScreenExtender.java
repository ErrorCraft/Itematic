package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

public class BookScreenExtender {
    @Mixin(BookScreen.Contents.class)
    public interface ContentsExtender {
        @Redirect(
            method = "create",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
                ordinal = 0
            )
        )
        private static boolean isOfForWrittenBookUseItemComponentCheck(ItemStack instance, Item item) {
            return instance.itematic$hasComponent(ItemComponentTypes.TEXT_HOLDER);
        }

        @Redirect(
            method = "create",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
                ordinal = 0
            ),
            slice = @Slice(
                from = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/Items;WRITABLE_BOOK:Lnet/minecraft/item/Item;",
                    opcode = Opcodes.GETSTATIC
                )
            )
        )
        private static boolean isOfForWritableBookUseItemComponentCheck(ItemStack instance, Item item) {
            return instance.itematic$hasComponent(ItemComponentTypes.WRITABLE);
        }
    }
}
