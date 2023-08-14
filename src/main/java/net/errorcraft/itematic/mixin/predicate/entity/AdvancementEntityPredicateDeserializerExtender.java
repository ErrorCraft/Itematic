package net.errorcraft.itematic.mixin.predicate.entity;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import net.errorcraft.itematic.access.predicate.entity.AdvancementEntityPredicateDeserializerAccess;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AdvancementEntityPredicateDeserializer.class)
public class AdvancementEntityPredicateDeserializerExtender implements AdvancementEntityPredicateDeserializerAccess {
    private DynamicRegistryManager registryManager;

    @ModifyArg(
        method = "loadConditions",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private DynamicOps<JsonElement> useRegistryOps(DynamicOps<JsonElement> ops) {
        return RegistryOps.of(ops, this.registryManager);
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }
}
