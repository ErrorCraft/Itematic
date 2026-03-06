package net.errorcraft.itematic.data.server;

import net.errorcraft.itematic.data.recipe.brewing.AmplifyBrewingRecipeBuilder;
import net.errorcraft.itematic.data.recipe.brewing.ModifyBrewingRecipeBuilder;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.potion.PotionKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
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

    public static class Generator extends RecipeGenerator {
        private final RegistryEntryLookup<Item> items;
        private final RegistryEntryLookup<Potion> potions;

        protected Generator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
            super(registries, exporter);
            this.items = registries.getOrThrow(RegistryKeys.ITEM);
            this.potions = registries.getOrThrow(RegistryKeys.POTION);
        }

        @Override
        public void generate() {
            this.amplify(ItemKeys.POTION, ItemKeys.GUNPOWDER, ItemKeys.SPLASH_POTION)
                .save(this.exporter);
            this.amplify(ItemKeys.SPLASH_POTION, ItemKeys.DRAGON_BREATH, ItemKeys.LINGERING_POTION)
                .remainder(this.items.getOrThrow(ItemKeys.GLASS_BOTTLE))
                .save(this.exporter);

            this.modify(PotionKeys.WATER, ItemKeys.FERMENTED_SPIDER_EYE, PotionKeys.WEAKNESS)
                .save(this.exporter);
            this.modify(PotionKeys.WATER, ItemKeys.GLOWSTONE_DUST, PotionKeys.THICK)
                .save(this.exporter);
            this.modify(PotionKeys.WATER, ItematicItemTags.MUNDANE_POTION_REAGENTS, PotionKeys.MUNDANE)
                .save(this.exporter);
            this.modify(PotionKeys.WATER, ItemKeys.NETHER_WART, PotionKeys.AWKWARD)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.BLAZE_POWDER, PotionKeys.STRENGTH)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.BREEZE_ROD, PotionKeys.WIND_CHARGED)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.COBWEB, PotionKeys.WEAVING)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.GHAST_TEAR, PotionKeys.REGENERATION)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.GLISTERING_MELON_SLICE, PotionKeys.HEALING)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.GOLDEN_CARROT, PotionKeys.NIGHT_VISION)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.MAGMA_CREAM, PotionKeys.FIRE_RESISTANCE)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.PHANTOM_MEMBRANE, PotionKeys.SLOW_FALLING)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.PUFFERFISH, PotionKeys.WATER_BREATHING)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.RABBIT_FOOT, PotionKeys.LEAPING)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.SLIME_BLOCK, PotionKeys.OOZING)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.SPIDER_EYE, PotionKeys.POISON)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.STONE, PotionKeys.INFESTED)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.SUGAR, PotionKeys.SWIFTNESS)
                .save(this.exporter);
            this.modify(PotionKeys.AWKWARD, ItemKeys.TURTLE_HELMET, PotionKeys.TURTLE_MASTER)
                .save(this.exporter);

            this.lengthen(PotionKeys.FIRE_RESISTANCE, PotionKeys.LONG_FIRE_RESISTANCE)
                .save(this.exporter);
            this.lengthen(PotionKeys.INVISIBILITY, PotionKeys.LONG_INVISIBILITY)
                .save(this.exporter);
            this.lengthen(PotionKeys.LEAPING, PotionKeys.LONG_LEAPING)
                .save(this.exporter);
            this.lengthen(PotionKeys.NIGHT_VISION, PotionKeys.LONG_NIGHT_VISION)
                .save(this.exporter);
            this.lengthen(PotionKeys.POISON, PotionKeys.LONG_POISON)
                .save(this.exporter);
            this.lengthen(PotionKeys.REGENERATION, PotionKeys.LONG_REGENERATION)
                .save(this.exporter);
            this.lengthen(PotionKeys.SLOW_FALLING, PotionKeys.LONG_SLOW_FALLING)
                .save(this.exporter);
            this.lengthen(PotionKeys.SLOWNESS, PotionKeys.LONG_SLOWNESS)
                .save(this.exporter);
            this.lengthen(PotionKeys.STRENGTH, PotionKeys.LONG_STRENGTH)
                .save(this.exporter);
            this.lengthen(PotionKeys.SWIFTNESS, PotionKeys.LONG_SWIFTNESS)
                .save(this.exporter);
            this.lengthen(PotionKeys.TURTLE_MASTER, PotionKeys.LONG_TURTLE_MASTER)
                .save(this.exporter);
            this.lengthen(PotionKeys.WATER_BREATHING, PotionKeys.LONG_WATER_BREATHING)
                .save(this.exporter);
            this.lengthen(PotionKeys.WEAKNESS, PotionKeys.LONG_WEAKNESS)
                .save(this.exporter);

            this.strengthen(PotionKeys.HARMING, PotionKeys.STRONG_HARMING)
                .save(this.exporter);
            this.strengthen(PotionKeys.HEALING, PotionKeys.STRONG_HEALING)
                .save(this.exporter);
            this.strengthen(PotionKeys.LEAPING, PotionKeys.STRONG_LEAPING)
                .save(this.exporter);
            this.strengthen(PotionKeys.POISON, PotionKeys.STRONG_POISON)
                .save(this.exporter);
            this.strengthen(PotionKeys.REGENERATION, PotionKeys.STRONG_REGENERATION)
                .save(this.exporter);
            this.strengthen(PotionKeys.SLOWNESS, PotionKeys.STRONG_SLOWNESS)
                .save(this.exporter);
            this.strengthen(PotionKeys.STRENGTH, PotionKeys.STRONG_STRENGTH)
                .save(this.exporter);
            this.strengthen(PotionKeys.SWIFTNESS, PotionKeys.STRONG_SWIFTNESS)
                .save(this.exporter);
            this.strengthen(PotionKeys.TURTLE_MASTER, PotionKeys.STRONG_TURTLE_MASTER)
                .save(this.exporter);

            this.negate(PotionKeys.HEALING, PotionKeys.HARMING)
                .save(this.exporter);
            this.negate(PotionKeys.LEAPING, PotionKeys.SLOWNESS)
                .save(this.exporter);
            this.negate(PotionKeys.LONG_LEAPING, PotionKeys.LONG_SLOWNESS)
                .save(this.exporter);
            this.negate(PotionKeys.LONG_NIGHT_VISION, PotionKeys.LONG_INVISIBILITY)
                .save(this.exporter);
            this.negate(PotionKeys.LONG_POISON, PotionKeys.HARMING)
                .save(this.exporter);
            this.negate(PotionKeys.LONG_SWIFTNESS, PotionKeys.LONG_SLOWNESS)
                .save(this.exporter);
            this.negate(PotionKeys.NIGHT_VISION, PotionKeys.INVISIBILITY)
                .save(this.exporter);
            this.negate(PotionKeys.POISON, PotionKeys.HARMING)
                .save(this.exporter);
            this.negate(PotionKeys.STRONG_HEALING, PotionKeys.STRONG_HARMING)
                .save(this.exporter);
            this.negate(PotionKeys.STRONG_POISON, PotionKeys.STRONG_HARMING)
                .save(this.exporter);
            this.negate(PotionKeys.SWIFTNESS, PotionKeys.SLOWNESS)
                .save(this.exporter);
        }

        private AmplifyBrewingRecipeBuilder amplify(RegistryKey<Item> base, RegistryKey<Item> reagent, RegistryKey<Item> result) {
            return new AmplifyBrewingRecipeBuilder(
                this.items.getOrThrow(base),
                RegistryEntryList.of(this.items.getOrThrow(reagent)),
                this.items.getOrThrow(result),
                result.getValue()
            );
        }

        private ModifyBrewingRecipeBuilder modify(RegistryKey<Potion> base, RegistryKey<Item> reagent, RegistryKey<Potion> result) {
            return this.modify(base, RegistryEntryList.of(this.items.getOrThrow(reagent)), result, potionName(result));
        }

        private ModifyBrewingRecipeBuilder modify(RegistryKey<Potion> base, TagKey<Item> reagent, RegistryKey<Potion> result) {
            return this.modify(base, this.items.getOrThrow(reagent), result, potionName(result));
        }

        private ModifyBrewingRecipeBuilder modify(RegistryKey<Potion> base, RegistryKey<Item> reagent, RegistryKey<Potion> result, String name) {
            return this.modify(base, RegistryEntryList.of(this.items.getOrThrow(reagent)), result, name);
        }

        private ModifyBrewingRecipeBuilder modify(RegistryKey<Potion> base, RegistryEntryList<Item> reagent, RegistryKey<Potion> result, String name) {
            return new ModifyBrewingRecipeBuilder(
                this.potions.getOrThrow(base),
                reagent,
                this.potions.getOrThrow(result),
                Identifier.ofVanilla(name)
            );
        }

        private ModifyBrewingRecipeBuilder lengthen(RegistryKey<Potion> from, RegistryKey<Potion> to) {
            return this.modify(from, ItemKeys.REDSTONE, to, potionName(from, to));
        }

        private ModifyBrewingRecipeBuilder strengthen(RegistryKey<Potion> from, RegistryKey<Potion> to) {
            return this.modify(from, ItemKeys.GLOWSTONE_DUST, to, potionName(from, to));
        }

        private ModifyBrewingRecipeBuilder negate(RegistryKey<Potion> from, RegistryKey<Potion> to) {
            return this.modify(from, ItemKeys.FERMENTED_SPIDER_EYE, to, potionName(from, to));
        }

        private static String potionName(RegistryKey<Potion> potion) {
            return potion.getValue().getPath() + "_potion";
        }

        private static String potionName(RegistryKey<Potion> from, RegistryKey<Potion> to) {
            return potionName(to) + "_from_" + potionName(from);
        }
    }
}
