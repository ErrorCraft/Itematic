package net.errorcraft.itematic.data.server.registry;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.data.util.RegistryUtil;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ItemGroupEntryProviderProvider extends FabricDynamicRegistryProvider {
    public ItemGroupEntryProviderProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        RegistryUtil.addAll(entries, registries.lookupOrThrow(ItematicRegistries.ITEM_GROUP_ENTRY_PROVIDER));
    }

    @Override
    public String getName() {
        return "Item Group Entry Providers";
    }
}
