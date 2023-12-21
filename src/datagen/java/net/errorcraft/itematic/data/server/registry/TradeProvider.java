package net.errorcraft.itematic.data.server.registry;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class TradeProvider extends FabricDynamicRegistryProvider {
    public TradeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        DynamicRegistryProviderUtil.addAll(entries, registries.getWrapperOrThrow(ItematicRegistryKeys.TRADE));
    }

    @Override
    public String getName() {
        return "Trades";
    }
}
