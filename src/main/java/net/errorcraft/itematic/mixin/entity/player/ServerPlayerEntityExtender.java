package net.errorcraft.itematic.mixin.entity.player;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityExtender {
    @Redirect(
        method = "playerTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        )
    )
    private Item playerTickGetItemReturnNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "playerTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;isNetworkSynced()Z"
        )
    )
    private boolean playerTickIsNetworkSyncedUseItemStackVersion(Item instance, @Local ItemStack stack) {
        return stack.itematic$isNetworkSynced();
    }
}
