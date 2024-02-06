package net.errorcraft.itematic.mixin.client.network;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.item.ItemGroupsAccessor;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerExtender {
    @Inject(
        method = "refreshTagBasedData",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroups;getSearchGroup()Lnet/minecraft/item/ItemGroup;"
        )
    )
    private void resetDisplayContext(CallbackInfo info) {
        ItemGroupsAccessor.setDisplayContext(null);
    }

    @Redirect(
        method = "onOpenWrittenBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForWrittenBookUseItemComponentCheck(ItemStack instance, Item item) {
        return instance.itematic$hasComponent(ItemComponentTypes.TEXT_HOLDER);
    }
}
