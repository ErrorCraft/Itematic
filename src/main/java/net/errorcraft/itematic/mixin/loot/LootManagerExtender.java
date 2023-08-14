package net.errorcraft.itematic.mixin.loot;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.loot.LootManagerAccess;
import net.errorcraft.itematic.loot.LootManagerUtil;
import net.minecraft.loot.LootManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(LootManager.class)
public class LootManagerExtender implements LootManagerAccess {
    private DynamicRegistryManager.Immutable registryManager;

    @Inject(
        method = "reload",
        at = @At("HEAD")
    )
    private void setTemporaryRegistryManager(ResourceReloader.Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor, CallbackInfoReturnable<CompletableFuture<Void>> info) {
        LootManagerUtil.setRegistryManager(this.registryManager);
    }

    @ModifyReturnValue(
        method = "reload",
        at = @At("TAIL")
    )
    private CompletableFuture<Void> resetTemporaryRegistryManager(CompletableFuture<Void> original, @Local(ordinal = 1) Executor applyExecutor) {
        return original.thenAcceptAsync(v -> LootManagerUtil.setRegistryManager(null), applyExecutor);
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager.Immutable registryManager) {
        this.registryManager = registryManager;
    }
}
