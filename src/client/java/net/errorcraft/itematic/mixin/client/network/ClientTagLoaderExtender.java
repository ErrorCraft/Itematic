package net.errorcraft.itematic.mixin.client.network;

import net.errorcraft.itematic.mixin.item.ItemGroupsAccessor;
import net.minecraft.client.network.ClientTagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientTagLoader.class)
public class ClientTagLoaderExtender {
    @Inject(
        method = "onDynamicTagsLoaded",
        at = @At("HEAD")
    )
    private static void resetItemGroupDisplayContext(CallbackInfo info) {
        ItemGroupsAccessor.setDisplayContext(null);
    }
}
