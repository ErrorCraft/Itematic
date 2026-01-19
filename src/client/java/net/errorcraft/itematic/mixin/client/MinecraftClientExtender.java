package net.errorcraft.itematic.mixin.client;

import net.errorcraft.itematic.access.client.MinecraftClientAccess;
import net.errorcraft.itematic.client.item.bar.ItemBarStyleLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientExtender implements MinecraftClientAccess {
    @Shadow
    @Final
    private ReloadableResourceManagerImpl resourceManager;

    @Unique
    private final ItemBarStyleLoader itemBarStyles = new ItemBarStyleLoader();

    @Inject(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/resource/ReloadableResourceManagerImpl;reload(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Ljava/util/List;)Lnet/minecraft/resource/ResourceReload;"
        )
    )
    private void addCustomLoaders(RunArgs args, CallbackInfo info) {
        this.resourceManager.registerReloader(this.itemBarStyles);
    }

    @Override
    public ItemBarStyleLoader itematic$itemBarStyles() {
        return this.itemBarStyles;
    }
}
