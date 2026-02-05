package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternBlockEntity.class)
public class LecternBlockEntityExtender {
    @Shadow
    ItemStack book;

    @Inject(
        method = "hasBook",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceWritableAndTextHolderBehavior(CallbackInfoReturnable<Boolean> info) {
        if (!this.book.itematic$hasBehavior(ItemComponentTypes.WRITABLE) && !this.book.itematic$hasBehavior(ItemComponentTypes.TEXT_HOLDER)) {
            info.setReturnValue(false);
        }
    }

    @Inject(
        method = "resolveBook",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceTextHolderBehavior(ItemStack book, PlayerEntity player, CallbackInfoReturnable<ItemStack> info) {
        if (!book.itematic$hasBehavior(ItemComponentTypes.TEXT_HOLDER)) {
            info.setReturnValue(book);
        }
    }
}
