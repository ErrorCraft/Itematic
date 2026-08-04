package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.potion.PotionTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PotionTagProvider extends FabricTagProvider<Potion> {
    public PotionTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.POTION, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        HolderLookup.RegistryLookup<Potion> potions = lookup.lookupOrThrow(Registries.POTION);
        PotionBrewing brewingRecipeRegistry = PotionBrewing.bootstrap(FeatureFlags.VANILLA_SET);
        this.builder(PotionTags.TRADEABLE)
            .addAll(getAll(potions, potion -> !potion.value().getEffects().isEmpty() && brewingRecipeRegistry.isBrewablePotion(potion)));
    }

    private static Stream<ResourceKey<Potion>> getAll(HolderLookup.RegistryLookup<Potion> registry, Predicate<Holder<Potion>> predicate) {
        return registry.listElements()
            .filter(predicate)
            .map(Holder.Reference::key);
    }
}
