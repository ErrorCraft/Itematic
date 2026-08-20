package net.errorcraft.itematic.data.server.registry;

import net.errorcraft.itematic.data.util.RegistryUtil;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import java.util.concurrent.CompletableFuture;

public class ItemProvider extends FabricDynamicRegistryProvider {
    public ItemProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        RegistryUtil.addAll(entries, registries.lookupOrThrow(Registries.ITEM));
    }

    @Override
    public String getName() {
        return "Items";
    }
}
