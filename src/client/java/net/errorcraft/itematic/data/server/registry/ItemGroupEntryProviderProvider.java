package net.errorcraft.itematic.data.server.registry;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ItemGroupEntryProviderProvider extends FabricDynamicRegistryProvider {
    public ItemGroupEntryProviderProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        DynamicRegistryProviderUtil.addAll(entries, registries.getWrapperOrThrow(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER));
    }

    @Override
    public String getName() {
        return "Item Group Entry Providers";
    }
}
