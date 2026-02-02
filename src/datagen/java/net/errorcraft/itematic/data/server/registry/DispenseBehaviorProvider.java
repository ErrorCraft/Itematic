package net.errorcraft.itematic.data.server.registry;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class DispenseBehaviorProvider extends FabricDynamicRegistryProvider {
    public DispenseBehaviorProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        DynamicRegistryProviderUtil.addAll(entries, registries.getOrThrow(ItematicRegistryKeys.DISPENSE_BEHAVIOR));
    }

    @Override
    public String getName() {
        return "Dispense Behaviors";
    }
}
