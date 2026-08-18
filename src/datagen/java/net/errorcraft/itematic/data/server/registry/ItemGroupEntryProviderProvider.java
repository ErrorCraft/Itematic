package net.errorcraft.itematic.data.server.registry;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class ItemGroupEntryProviderProvider extends FabricDynamicRegistryProvider {
    public ItemGroupEntryProviderProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        DynamicRegistryProviderUtil.addAll(entries, registries.lookupOrThrow(ItematicRegistries.ITEM_GROUP_ENTRY_PROVIDER));
    }

    @Override
    public String getName() {
        return "Item Group Entry Providers";
    }
}
