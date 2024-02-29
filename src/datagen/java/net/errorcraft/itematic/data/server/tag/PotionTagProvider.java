package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.potion.PotionTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class PotionTagProvider extends FabricTagProvider<Potion> {
    public PotionTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.POTION, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        RegistryWrapper.Impl<Potion> potions = arg.getWrapperOrThrow(RegistryKeys.POTION);
        this.getOrCreateTagBuilder(PotionTags.TRADEABLE)
            .add(getAll(potions, potion -> !potion.value().getEffects().isEmpty() && BrewingRecipeRegistry.isBrewable(potion)));
    }

    private static Identifier[] getAll(RegistryWrapper.Impl<Potion> registry, Predicate<RegistryEntry<Potion>> predicate) {
        return registry.streamEntries()
            .filter(predicate)
            .map(RegistryEntry.Reference::registryKey)
            .map(RegistryKey::getValue)
            .toArray(Identifier[]::new);
    }
}
