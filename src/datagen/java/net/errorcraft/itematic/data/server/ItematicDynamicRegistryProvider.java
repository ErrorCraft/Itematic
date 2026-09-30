package net.errorcraft.itematic.data.server;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

public class ItematicDynamicRegistryProvider extends FabricDynamicRegistryProvider {
    public ItematicDynamicRegistryProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        addAll(entries, registries.lookupOrThrow(Registries.ITEM));
        addAll(entries, registries.lookupOrThrow(ItematicRegistries.ITEM_GROUP_ENTRY_PROVIDER));
        addAll(entries, registries.lookupOrThrow(ItematicRegistries.ACTION));
        addAll(entries, registries.lookupOrThrow(ItematicRegistries.DISPENSE_BEHAVIOR));
        addAll(entries, registries.lookupOrThrow(ItematicRegistries.ENTITY_SPAWN_RULE_SET));
    }

    @Override
    public String getName() {
        return "Itematic Dynamic Registries";
    }

    private static <T> void addAll(Entries entries, HolderLookup.RegistryLookup<T> registry) {
        registry.listElementIds().forEach(key -> entries.add(registry, key));
    }
}
