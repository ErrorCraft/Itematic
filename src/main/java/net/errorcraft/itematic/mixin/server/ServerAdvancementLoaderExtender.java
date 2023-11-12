package net.errorcraft.itematic.mixin.server;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import net.errorcraft.itematic.access.server.ServerAdvancementLoaderAccess;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.ServerAdvancementLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerAdvancementLoader.class)
public class ServerAdvancementLoaderExtender implements ServerAdvancementLoaderAccess {
    @Unique
    private DynamicRegistryManager registryManager;

    @ModifyArg(
        method = "method_20723",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"
        ),
        remap = false
    )
    private DynamicOps<JsonElement> setDynamicRegistryManagerAdvancementEntityPredicateDeserializer(DynamicOps<JsonElement> ops) {
        return RegistryOps.of(ops, this.registryManager);
    }

    @Override
    public void itematic$setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }
}
