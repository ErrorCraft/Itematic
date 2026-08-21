package net.errorcraft.itematic.data.server;

import net.errorcraft.itematic.data.recipe.brewing.AmplifyBrewingRecipeBuilder;
import net.errorcraft.itematic.data.recipe.brewing.ModifyBrewingRecipeBuilder;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.references.PotionIds;
import net.errorcraft.itematic.tags.ItematicItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;

import java.util.concurrent.CompletableFuture;

public class ItematicRecipeProvider extends FabricRecipeProvider {
    public ItematicRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new Provider(registries, output);
    }

    @Override
    public String getName() {
        return "Recipes";
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return identifier;
    }

    public static class Provider extends RecipeProvider {
        private final HolderGetter<Item> items;
        private final HolderGetter<Potion> potions;

        private Provider(HolderLookup.Provider registries, RecipeOutput output) {
            super(registries, output);
            this.items = registries.lookupOrThrow(Registries.ITEM);
            this.potions = registries.lookupOrThrow(Registries.POTION);
        }

        @Override
        public void buildRecipes() {
            this.amplify(ItemIds.POTION, ItemIds.GUNPOWDER, ItemIds.SPLASH_POTION)
                .save(this.output);
            this.amplify(ItemIds.SPLASH_POTION, ItemIds.DRAGON_BREATH, ItemIds.LINGERING_POTION)
                .remainder(this.items.getOrThrow(ItemIds.GLASS_BOTTLE))
                .save(this.output);

            this.modify(PotionIds.WATER, ItemIds.FERMENTED_SPIDER_EYE, PotionIds.WEAKNESS)
                .save(this.output);
            this.modify(PotionIds.WATER, ItemIds.GLOWSTONE_DUST, PotionIds.THICK)
                .save(this.output);
            this.modify(PotionIds.WATER, ItematicItemTags.MUNDANE_POTION_REAGENTS, PotionIds.MUNDANE)
                .save(this.output);
            this.modify(PotionIds.WATER, ItemIds.NETHER_WART, PotionIds.AWKWARD)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.BLAZE_POWDER, PotionIds.STRENGTH)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.BREEZE_ROD, PotionIds.WIND_CHARGED)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.COBWEB, PotionIds.WEAVING)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.GHAST_TEAR, PotionIds.REGENERATION)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.GLISTERING_MELON_SLICE, PotionIds.HEALING)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.GOLDEN_CARROT, PotionIds.NIGHT_VISION)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.MAGMA_CREAM, PotionIds.FIRE_RESISTANCE)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.PHANTOM_MEMBRANE, PotionIds.SLOW_FALLING)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.PUFFERFISH, PotionIds.WATER_BREATHING)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.RABBIT_FOOT, PotionIds.LEAPING)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.SLIME_BLOCK, PotionIds.OOZING)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.SPIDER_EYE, PotionIds.POISON)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.STONE, PotionIds.INFESTED)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.SUGAR, PotionIds.SWIFTNESS)
                .save(this.output);
            this.modify(PotionIds.AWKWARD, ItemIds.TURTLE_HELMET, PotionIds.TURTLE_MASTER)
                .save(this.output);

            this.lengthen(PotionIds.FIRE_RESISTANCE, PotionIds.LONG_FIRE_RESISTANCE)
                .save(this.output);
            this.lengthen(PotionIds.INVISIBILITY, PotionIds.LONG_INVISIBILITY)
                .save(this.output);
            this.lengthen(PotionIds.LEAPING, PotionIds.LONG_LEAPING)
                .save(this.output);
            this.lengthen(PotionIds.NIGHT_VISION, PotionIds.LONG_NIGHT_VISION)
                .save(this.output);
            this.lengthen(PotionIds.POISON, PotionIds.LONG_POISON)
                .save(this.output);
            this.lengthen(PotionIds.REGENERATION, PotionIds.LONG_REGENERATION)
                .save(this.output);
            this.lengthen(PotionIds.SLOW_FALLING, PotionIds.LONG_SLOW_FALLING)
                .save(this.output);
            this.lengthen(PotionIds.SLOWNESS, PotionIds.LONG_SLOWNESS)
                .save(this.output);
            this.lengthen(PotionIds.STRENGTH, PotionIds.LONG_STRENGTH)
                .save(this.output);
            this.lengthen(PotionIds.SWIFTNESS, PotionIds.LONG_SWIFTNESS)
                .save(this.output);
            this.lengthen(PotionIds.TURTLE_MASTER, PotionIds.LONG_TURTLE_MASTER)
                .save(this.output);
            this.lengthen(PotionIds.WATER_BREATHING, PotionIds.LONG_WATER_BREATHING)
                .save(this.output);
            this.lengthen(PotionIds.WEAKNESS, PotionIds.LONG_WEAKNESS)
                .save(this.output);

            this.strengthen(PotionIds.HARMING, PotionIds.STRONG_HARMING)
                .save(this.output);
            this.strengthen(PotionIds.HEALING, PotionIds.STRONG_HEALING)
                .save(this.output);
            this.strengthen(PotionIds.LEAPING, PotionIds.STRONG_LEAPING)
                .save(this.output);
            this.strengthen(PotionIds.POISON, PotionIds.STRONG_POISON)
                .save(this.output);
            this.strengthen(PotionIds.REGENERATION, PotionIds.STRONG_REGENERATION)
                .save(this.output);
            this.strengthen(PotionIds.SLOWNESS, PotionIds.STRONG_SLOWNESS)
                .save(this.output);
            this.strengthen(PotionIds.STRENGTH, PotionIds.STRONG_STRENGTH)
                .save(this.output);
            this.strengthen(PotionIds.SWIFTNESS, PotionIds.STRONG_SWIFTNESS)
                .save(this.output);
            this.strengthen(PotionIds.TURTLE_MASTER, PotionIds.STRONG_TURTLE_MASTER)
                .save(this.output);

            this.negate(PotionIds.HEALING, PotionIds.HARMING)
                .save(this.output);
            this.negate(PotionIds.LEAPING, PotionIds.SLOWNESS)
                .save(this.output);
            this.negate(PotionIds.LONG_LEAPING, PotionIds.LONG_SLOWNESS)
                .save(this.output);
            this.negate(PotionIds.LONG_NIGHT_VISION, PotionIds.LONG_INVISIBILITY)
                .save(this.output);
            this.negate(PotionIds.LONG_POISON, PotionIds.HARMING)
                .save(this.output);
            this.negate(PotionIds.LONG_SWIFTNESS, PotionIds.LONG_SLOWNESS)
                .save(this.output);
            this.negate(PotionIds.NIGHT_VISION, PotionIds.INVISIBILITY)
                .save(this.output);
            this.negate(PotionIds.POISON, PotionIds.HARMING)
                .save(this.output);
            this.negate(PotionIds.STRONG_HEALING, PotionIds.STRONG_HARMING)
                .save(this.output);
            this.negate(PotionIds.STRONG_POISON, PotionIds.STRONG_HARMING)
                .save(this.output);
            this.negate(PotionIds.SWIFTNESS, PotionIds.SLOWNESS)
                .save(this.output);
        }

        private AmplifyBrewingRecipeBuilder amplify(ResourceKey<Item> base, ResourceKey<Item> reagent, ResourceKey<Item> result) {
            return new AmplifyBrewingRecipeBuilder(
                this.items.getOrThrow(base),
                HolderSet.direct(this.items.getOrThrow(reagent)),
                this.items.getOrThrow(result),
                result.identifier()
            );
        }

        private ModifyBrewingRecipeBuilder modify(ResourceKey<Potion> base, ResourceKey<Item> reagent, ResourceKey<Potion> result) {
            return this.modify(base, HolderSet.direct(this.items.getOrThrow(reagent)), result, potionName(result));
        }

        private ModifyBrewingRecipeBuilder modify(ResourceKey<Potion> base, TagKey<Item> reagent, ResourceKey<Potion> result) {
            return this.modify(base, this.items.getOrThrow(reagent), result, potionName(result));
        }

        private ModifyBrewingRecipeBuilder modify(ResourceKey<Potion> base, ResourceKey<Item> reagent, ResourceKey<Potion> result, String name) {
            return this.modify(base, HolderSet.direct(this.items.getOrThrow(reagent)), result, name);
        }

        private ModifyBrewingRecipeBuilder modify(ResourceKey<Potion> base, HolderSet<Item> reagent, ResourceKey<Potion> result, String name) {
            return new ModifyBrewingRecipeBuilder(
                this.potions.getOrThrow(base),
                reagent,
                this.potions.getOrThrow(result),
                Identifier.withDefaultNamespace(name)
            );
        }

        private ModifyBrewingRecipeBuilder lengthen(ResourceKey<Potion> from, ResourceKey<Potion> to) {
            return this.modify(from, ItemIds.REDSTONE, to, potionName(from, to));
        }

        private ModifyBrewingRecipeBuilder strengthen(ResourceKey<Potion> from, ResourceKey<Potion> to) {
            return this.modify(from, ItemIds.GLOWSTONE_DUST, to, potionName(from, to));
        }

        private ModifyBrewingRecipeBuilder negate(ResourceKey<Potion> from, ResourceKey<Potion> to) {
            return this.modify(from, ItemIds.FERMENTED_SPIDER_EYE, to, potionName(from, to));
        }

        private static String potionName(ResourceKey<Potion> potion) {
            return potion.identifier().getPath() + "_potion";
        }

        private static String potionName(ResourceKey<Potion> from, ResourceKey<Potion> to) {
            return potionName(to) + "_from_" + potionName(from);
        }
    }
}
