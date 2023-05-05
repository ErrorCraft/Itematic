package net.errorcraft.itematic.mixin.server;

import net.errorcraft.itematic.access.registry.DynamicRegistryManagerAccess;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.ServerAdvancementLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerAdvancementLoader.class)
public class ServerAdvancementLoaderExtender implements DynamicRegistryManagerAccess {
    private DynamicRegistryManager registryManager;

    @ModifyArg(
        method = "method_20723",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/advancement/Advancement$Builder;fromJson(Lcom/google/gson/JsonObject;Lnet/minecraft/predicate/entity/AdvancementEntityPredicateDeserializer;)Lnet/minecraft/advancement/Advancement$Builder;"
        ),
        remap = false
    )
    private AdvancementEntityPredicateDeserializer setDynamicRegistryManagerAdvancementEntityPredicateDeserializer(AdvancementEntityPredicateDeserializer predicateDeserializer) {
        predicateDeserializer.setRegistryManager(this.registryManager);
        return predicateDeserializer;
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }
}
