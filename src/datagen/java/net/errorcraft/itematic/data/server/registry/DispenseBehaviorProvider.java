package net.errorcraft.itematic.data.server.registry;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class DispenseBehaviorProvider extends FabricDynamicRegistryProvider {
    public DispenseBehaviorProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        DynamicRegistryProviderUtil.addAll(entries, registries.lookupOrThrow(ItematicRegistryKeys.DISPENSE_BEHAVIOR));
    }

    @Override
    public String getName() {
        return "Dispense Behaviors";
    }
}
