package net.errorcraft.itematic.mixin.client;

import net.errorcraft.itematic.access.client.MinecraftAccess;
import net.errorcraft.itematic.client.resources.item.bar.ItemBarStyleManager;
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
public class MinecraftExtender implements MinecraftAccess {
    @Shadow
    @Final
    private ReloadableResourceManager resourceManager;

    @Unique
    private final ItemBarStyleManager itemBarStyles = new ItemBarStyleManager();

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
    public ItemBarStyleManager itematic$itemBarStyles() {
        return this.itemBarStyles;
    }
}
