package net.errorcraft.itematic.mixin.server;

import com.google.gson.JsonElement;
import net.errorcraft.itematic.access.server.ServerAdvancementLoaderAccess;
import net.errorcraft.itematic.server.ServerAdvancementLoaderUtil;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ServerAdvancementLoader.class)
public class ServerAdvancementLoaderExtender implements ServerAdvancementLoaderAccess {
    private DynamicRegistryManager registryManager;

    @Inject(
        method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
        at = @At("HEAD")
    )
    private void setTemporaryRegistryManager(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        ServerAdvancementLoaderUtil.setRegistryManager(this.registryManager);
    }

    @Inject(
        method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V",
        at = @At("TAIL")
    )
    private void resetTemporaryRegistryManager(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        ServerAdvancementLoaderUtil.setRegistryManager(null);
    }

    @ModifyArg(
        method = "method_20723",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/advancement/Advancement;fromJson(Lcom/google/gson/JsonObject;Lnet/minecraft/predicate/entity/AdvancementEntityPredicateDeserializer;)Lnet/minecraft/advancement/Advancement;"
        ),
        remap = false
    )
    private AdvancementEntityPredicateDeserializer setDynamicRegistryManagerAdvancementEntityPredicateDeserializer(AdvancementEntityPredicateDeserializer predicateDeserializer) {
        predicateDeserializer.setRegistryManager(this.registryManager);
        return predicateDeserializer;
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }
}
