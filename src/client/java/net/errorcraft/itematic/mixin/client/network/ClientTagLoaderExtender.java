package net.errorcraft.itematic.mixin.client.network;

import net.minecraft.client.network.ClientTagLoader;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientTagLoader.class)
public class ClientTagLoaderExtender {
    // TODO: Check where this was moved to, might be in net.minecraft.client.search.SearchManager::method_60355 (from onSynchronizeTags)
    //  This might no longer be needed in the first place
//    @Inject(
//        method = "onDynamicTagsLoaded",
//        at = @At("HEAD")
//    )
//    private static void resetItemGroupDisplayContext(CallbackInfo info) {
//        ItemGroupsAccessor.setDisplayContext(null);
//    }
}
