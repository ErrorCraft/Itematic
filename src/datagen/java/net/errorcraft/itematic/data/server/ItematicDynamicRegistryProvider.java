package net.errorcraft.itematic.data.server;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.data.util.RegistryUtil;
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
        RegistryUtil.addAll(entries, registries.lookupOrThrow(Registries.ITEM));
        RegistryUtil.addAll(entries, registries.lookupOrThrow(ItematicRegistries.ITEM_GROUP_ENTRY_PROVIDER));
        RegistryUtil.addAll(entries, registries.lookupOrThrow(ItematicRegistries.ACTION));
        RegistryUtil.addAll(entries, registries.lookupOrThrow(ItematicRegistries.DISPENSE_BEHAVIOR));
        RegistryUtil.addAll(entries, registries.lookupOrThrow(ItematicRegistries.ENTITY_SPAWN_RULE_SET));
    }

    @Override
    public String getName() {
        return "Itematic Registries";
    }
}
