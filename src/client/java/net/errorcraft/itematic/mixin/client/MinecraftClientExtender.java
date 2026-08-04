package net.errorcraft.itematic.mixin.client;

import net.errorcraft.itematic.access.client.MinecraftClientAccess;
import net.errorcraft.itematic.client.item.bar.ItemBarStyleLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientExtender implements MinecraftClientAccess {
    @Shadow
    @Final
    private ReloadableResourceManager resourceManager;

    @Unique
    private final ItemBarStyleLoader itemBarStyles = new ItemBarStyleLoader();

    @Inject(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/packs/resources/ReloadableResourceManager;createReload(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Ljava/util/List;)Lnet/minecraft/server/packs/resources/ReloadInstance;"
        )
    )
    private void addCustomLoaders(GameConfig args, CallbackInfo info) {
        this.resourceManager.registerReloadListener(this.itemBarStyles);
    }

    @Override
    public ItemBarStyleLoader itematic$itemBarStyles() {
        return this.itemBarStyles;
    }
}
