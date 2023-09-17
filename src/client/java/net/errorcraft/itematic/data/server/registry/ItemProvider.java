package net.errorcraft.itematic.data.server.registry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class ItemProvider extends FabricDynamicRegistryProvider {
    public ItemProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        DynamicRegistryProviderUtil.addAll(entries, registries.getWrapperOrThrow(RegistryKeys.ITEM));
    }

    @Override
    public String getName() {
        return "Items";
    }
}
