package net.errorcraft.itematic.data.server;

import net.errorcraft.itematic.data.recipe.brewing.AmplifyBrewingRecipeBuilder;
import net.errorcraft.itematic.data.recipe.brewing.ModifyBrewingRecipeBuilder;
import net.errorcraft.itematic.potion.PotionKeys;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.tags.ItematicItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected net.minecraft.data.recipes.RecipeProvider createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput recipeExporter) {
        return new Generator(wrapperLookup, recipeExporter);
    }

    @Override
    public String getName() {
        return "Recipes";
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return identifier;
    }

    public static class Generator extends net.minecraft.data.recipes.RecipeProvider {
        private final HolderGetter<Item> items;
        private final HolderGetter<Potion> potions;

        protected Generator(HolderLookup.Provider registries, RecipeOutput exporter) {
            super(registries, exporter);
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

            this.modify(PotionKeys.WATER, ItemIds.FERMENTED_SPIDER_EYE, PotionKeys.WEAKNESS)
                .save(this.output);
            this.modify(PotionKeys.WATER, ItemIds.GLOWSTONE_DUST, PotionKeys.THICK)
                .save(this.output);
            this.modify(PotionKeys.WATER, ItematicItemTags.MUNDANE_POTION_REAGENTS, PotionKeys.MUNDANE)
                .save(this.output);
            this.modify(PotionKeys.WATER, ItemIds.NETHER_WART, PotionKeys.AWKWARD)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.BLAZE_POWDER, PotionKeys.STRENGTH)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.BREEZE_ROD, PotionKeys.WIND_CHARGED)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.COBWEB, PotionKeys.WEAVING)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.GHAST_TEAR, PotionKeys.REGENERATION)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.GLISTERING_MELON_SLICE, PotionKeys.HEALING)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.GOLDEN_CARROT, PotionKeys.NIGHT_VISION)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.MAGMA_CREAM, PotionKeys.FIRE_RESISTANCE)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.PHANTOM_MEMBRANE, PotionKeys.SLOW_FALLING)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.PUFFERFISH, PotionKeys.WATER_BREATHING)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.RABBIT_FOOT, PotionKeys.LEAPING)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.SLIME_BLOCK, PotionKeys.OOZING)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.SPIDER_EYE, PotionKeys.POISON)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.STONE, PotionKeys.INFESTED)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.SUGAR, PotionKeys.SWIFTNESS)
                .save(this.output);
            this.modify(PotionKeys.AWKWARD, ItemIds.TURTLE_HELMET, PotionKeys.TURTLE_MASTER)
                .save(this.output);

            this.lengthen(PotionKeys.FIRE_RESISTANCE, PotionKeys.LONG_FIRE_RESISTANCE)
                .save(this.output);
            this.lengthen(PotionKeys.INVISIBILITY, PotionKeys.LONG_INVISIBILITY)
                .save(this.output);
            this.lengthen(PotionKeys.LEAPING, PotionKeys.LONG_LEAPING)
                .save(this.output);
            this.lengthen(PotionKeys.NIGHT_VISION, PotionKeys.LONG_NIGHT_VISION)
                .save(this.output);
            this.lengthen(PotionKeys.POISON, PotionKeys.LONG_POISON)
                .save(this.output);
            this.lengthen(PotionKeys.REGENERATION, PotionKeys.LONG_REGENERATION)
                .save(this.output);
            this.lengthen(PotionKeys.SLOW_FALLING, PotionKeys.LONG_SLOW_FALLING)
                .save(this.output);
            this.lengthen(PotionKeys.SLOWNESS, PotionKeys.LONG_SLOWNESS)
                .save(this.output);
            this.lengthen(PotionKeys.STRENGTH, PotionKeys.LONG_STRENGTH)
                .save(this.output);
            this.lengthen(PotionKeys.SWIFTNESS, PotionKeys.LONG_SWIFTNESS)
                .save(this.output);
            this.lengthen(PotionKeys.TURTLE_MASTER, PotionKeys.LONG_TURTLE_MASTER)
                .save(this.output);
            this.lengthen(PotionKeys.WATER_BREATHING, PotionKeys.LONG_WATER_BREATHING)
                .save(this.output);
            this.lengthen(PotionKeys.WEAKNESS, PotionKeys.LONG_WEAKNESS)
                .save(this.output);

            this.strengthen(PotionKeys.HARMING, PotionKeys.STRONG_HARMING)
                .save(this.output);
            this.strengthen(PotionKeys.HEALING, PotionKeys.STRONG_HEALING)
                .save(this.output);
            this.strengthen(PotionKeys.LEAPING, PotionKeys.STRONG_LEAPING)
                .save(this.output);
            this.strengthen(PotionKeys.POISON, PotionKeys.STRONG_POISON)
                .save(this.output);
            this.strengthen(PotionKeys.REGENERATION, PotionKeys.STRONG_REGENERATION)
                .save(this.output);
            this.strengthen(PotionKeys.SLOWNESS, PotionKeys.STRONG_SLOWNESS)
                .save(this.output);
            this.strengthen(PotionKeys.STRENGTH, PotionKeys.STRONG_STRENGTH)
                .save(this.output);
            this.strengthen(PotionKeys.SWIFTNESS, PotionKeys.STRONG_SWIFTNESS)
                .save(this.output);
            this.strengthen(PotionKeys.TURTLE_MASTER, PotionKeys.STRONG_TURTLE_MASTER)
                .save(this.output);

            this.negate(PotionKeys.HEALING, PotionKeys.HARMING)
                .save(this.output);
            this.negate(PotionKeys.LEAPING, PotionKeys.SLOWNESS)
                .save(this.output);
            this.negate(PotionKeys.LONG_LEAPING, PotionKeys.LONG_SLOWNESS)
                .save(this.output);
            this.negate(PotionKeys.LONG_NIGHT_VISION, PotionKeys.LONG_INVISIBILITY)
                .save(this.output);
            this.negate(PotionKeys.LONG_POISON, PotionKeys.HARMING)
                .save(this.output);
            this.negate(PotionKeys.LONG_SWIFTNESS, PotionKeys.LONG_SLOWNESS)
                .save(this.output);
            this.negate(PotionKeys.NIGHT_VISION, PotionKeys.INVISIBILITY)
                .save(this.output);
            this.negate(PotionKeys.POISON, PotionKeys.HARMING)
                .save(this.output);
            this.negate(PotionKeys.STRONG_HEALING, PotionKeys.STRONG_HARMING)
                .save(this.output);
            this.negate(PotionKeys.STRONG_POISON, PotionKeys.STRONG_HARMING)
                .save(this.output);
            this.negate(PotionKeys.SWIFTNESS, PotionKeys.SLOWNESS)
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
