package net.errorcraft.itematic.mixin.server;

import com.mojang.datafixers.DataFixer;
import net.errorcraft.itematic.access.entity.boss.BossBarManagerAccess;
import net.minecraft.entity.boss.BossBarManager;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.util.ApiServices;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

@Mixin(MinecraftServer.class)
public class MinecraftServerExtender {
    @Shadow
    @Final
    private CombinedDynamicRegistries<ServerDynamicRegistryType> combinedDynamicRegistries;

    @Shadow
    @Final
    private BossBarManager bossBarManager;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setDynamicRegistries(Thread serverThread, LevelStorage.Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader, Proxy proxy, DataFixer dataFixer, ApiServices apiServices, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo info) {
        ((BossBarManagerAccess) this.bossBarManager).itematic$setLookup(this.combinedDynamicRegistries.getCombinedRegistryManager());
    }
}
