package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
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
        if (!this.book.itematic$hasBehavior(ItemBehaviorType.WRITABLE) && !this.book.itematic$hasBehavior(ItemBehaviorType.TEXT_HOLDER)) {
            info.setReturnValue(false);
        }
    }

    @Inject(
        method = "resolveBook",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceTextHolderBehavior(ItemStack book, Player player, CallbackInfoReturnable<ItemStack> info) {
        if (!book.itematic$hasBehavior(ItemBehaviorType.TEXT_HOLDER)) {
            info.setReturnValue(book);
        }
    }
}
