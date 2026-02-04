package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LecternBlockEntity.class)
public class LecternBlockEntityExtender {
    @Redirect(
        method = "hasBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfForWritableBookUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasBehavior(ItemComponentTypes.WRITABLE);
    }

    @Redirect(
        method = { "hasBook", "resolveBook" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;WRITTEN_BOOK:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForWrittenBookUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasBehavior(ItemComponentTypes.TEXT_HOLDER);
    }
}
