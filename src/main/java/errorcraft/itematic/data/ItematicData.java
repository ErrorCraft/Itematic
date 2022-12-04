package errorcraft.itematic.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ItematicData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ItemProvider::new);
    }

    private static <T> void addAll(FabricDynamicRegistryProvider.Entries entries, RegistryWrapper.Impl<T> registry) {
        registry.streamKeys().forEach(key -> entries.add(registry, key));
    }

    private static class ItemProvider extends FabricDynamicRegistryProvider {
        public ItemProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            addAll(entries, registries.getWrapperOrThrow(RegistryKeys.ITEM));
        }

        @Override
        public String getName() {
            return "Itematic Items";
        }
    }
}
