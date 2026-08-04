package net.errorcraft.itematic.mixin.client.network;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class ClientPlayerEntityExtender {
    @Inject(
        method = "openItemGui",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceTextHolderBehavior(ItemStack book, InteractionHand hand, CallbackInfo info) {
        if (!book.itematic$hasBehavior(ItemComponentTypes.TEXT_HOLDER)) {
            info.cancel();
        }
    }
}
