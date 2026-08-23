package net.errorcraft.itematic.data.server.registry;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.data.util.RegistryUtil;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class DispenseBehaviorProvider extends FabricDynamicRegistryProvider {
    public DispenseBehaviorProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        RegistryUtil.addAll(entries, registries.lookupOrThrow(ItematicRegistries.DISPENSE_BEHAVIOR));
    }

    @Override
    public String getName() {
        return "Dispense Behaviors";
    }
}
