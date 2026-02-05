package net.errorcraft.itematic.mixin.client.network;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityExtender {
    @Inject(
        method = "useBook",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkPresenceTextHolderBehavior(ItemStack book, Hand hand, CallbackInfo info) {
        if (!book.itematic$hasBehavior(ItemComponentTypes.TEXT_HOLDER)) {
            info.cancel();
        }
    }
}
