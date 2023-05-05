package net.errorcraft.itematic.mixin.predicate.entity;

import net.errorcraft.itematic.access.registry.DynamicRegistryManagerAccess;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AdvancementEntityPredicateDeserializer.class)
public class AdvancementEntityPredicateDeserializerExtender implements DynamicRegistryManagerAccess {
    private DynamicRegistryManager registryManager;

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }
}
