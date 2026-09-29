package net.errorcraft.itematic.data.server;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import net.errorcraft.itematic.mixin.data.recipes.BrewingRecipeBuilderAccessor;
import net.errorcraft.itematic.world.item.ItemStackTemplates;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.PotionsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.BrewingRecipe;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PotionIngredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModifiedRecipeProvider extends FabricCodecDataProvider<Recipe<?>> {
    public ModifiedRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.DATA_PACK, "recipe", Recipe.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Recipe<?>> provider, HolderLookup.Provider lookup) {
        new BrewingRecipeProvider(provider, lookup).configure();
        HolderLookup.RegistryLookup<Item> items = lookup.lookupOrThrow(Registries.ITEM);
        provider.accept(
            Identifier.withDefaultNamespace("honey_block"),
            shapedRecipe(RecipeCategory.FOOD, items.getOrThrow(BlockItemIds.HONEY_BLOCK.item()))
                .input('#', items.getOrThrow(ItemIds.HONEY_BOTTLE), items.getOrThrow(ItemIds.GLASS_BOTTLE))
                .pattern("##")
                .pattern("##")
                .build()
        );
        provider.accept(
            Identifier.withDefaultNamespace("sugar_from_honey_bottle"),
            shapelessRecipe(RecipeCategory.MISC, items.getOrThrow(ItemIds.SUGAR), 3)
                .input(items.getOrThrow(ItemIds.HONEY_BOTTLE), 1, items.getOrThrow(ItemIds.GLASS_BOTTLE))
                .build()
        );
        provider.accept(
            Identifier.withDefaultNamespace("cake"),
            shapedRecipe(RecipeCategory.FOOD, items.getOrThrow(BlockItemIds.CAKE.item()))
                .input('A', items.getOrThrow(ItemIds.MILK_BUCKET), items.getOrThrow(ItemIds.BUCKET))
                .input('B', items.getOrThrow(ItemIds.SUGAR))
                .input('C', items.getOrThrow(ItemIds.WHEAT))
                .input('E', items.getOrThrow(ItemIds.EGG))
                .pattern("AAA")
                .pattern("BEB")
                .pattern("CCC")
                .build()
        );
    }

    @Override
    public String getName() {
        return "Modified Recipes";
    }

    private static ShapelessRecipeBuilder shapelessRecipe(RecipeCategory category, Holder<Item> result, int count) {
        return new ShapelessRecipeBuilder(ItemStackTemplates.of(result, count), category);
    }

    private static ShapedRecipeBuilder shapedRecipe(RecipeCategory category, Holder<Item> result) {
        return new ShapedRecipeBuilder(ItemStackTemplates.of(result), category);
    }

    private static class ShapelessRecipeBuilder {
        private final ItemStackTemplate result;
        private final RecipeCategory category;
        private final List<Ingredient> inputs = new ArrayList<>();

        private ShapelessRecipeBuilder(ItemStackTemplate result, RecipeCategory category) {
            this.result = result;
            this.category = category;
        }

        public ShapelessRecipe build() {
            return new ShapelessRecipe(
                new Recipe.CommonInfo(true),
                new CraftingRecipe.CraftingBookInfo(
                    RecipeBuilder.determineCraftingBookCategory(this.category),
                    ""
                ),
                this.result,
                this.inputs
            );
        }

        public ShapelessRecipeBuilder input(Holder<Item> input, int count, Holder<Item> remainder) {
            for (int i = 0; i < count; i++) {
                Ingredient ingredient = Ingredient.of(HolderSet.direct(input));
                ingredient.itematic$setRemainder(Optional.of(ItemStackTemplates.of(remainder)));
                this.inputs.add(ingredient);
            }

            return this;
        }
    }

    private static class ShapedRecipeBuilder {
        private final ItemStackTemplate result;
        private final RecipeCategory category;
        private final Char2ObjectMap<Ingredient> inputs = new Char2ObjectOpenHashMap<>();
        private final List<String> pattern = new ArrayList<>();

        private ShapedRecipeBuilder(ItemStackTemplate result, RecipeCategory category) {
            this.result = result;
            this.category = category;
        }

        public ShapedRecipe build() {
            return new ShapedRecipe(
                new Recipe.CommonInfo(true),
                new CraftingRecipe.CraftingBookInfo(
                    RecipeBuilder.determineCraftingBookCategory(this.category),
                    ""
                ),
                ShapedRecipePattern.of(this.inputs, this.pattern),
                this.result
            );
        }

        public ShapedRecipeBuilder input(char key, Holder<Item> input) {
            this.inputs.put(key, Ingredient.of(HolderSet.direct(input)));
            return this;
        }

        public ShapedRecipeBuilder input(char key, Holder<Item> input, Holder<Item> remainder) {
            Ingredient ingredient = Ingredient.of(HolderSet.direct(input));
            ingredient.itematic$setRemainder(Optional.of(ItemStackTemplates.of(remainder)));
            this.inputs.put(key, ingredient);
            return this;
        }

        public ShapedRecipeBuilder pattern(String pattern) {
            this.pattern.add(pattern);
            return this;
        }
    }

    private static class BrewingRecipeProvider {
        private static final Set<Holder<Potion>> POTIONS_WITHOUT_RECIPE = Set.of(Potions.LUCK);
        private final BiConsumer<Identifier, Recipe<?>> provider;
        private final HolderLookup.RegistryLookup<Potion> potions;
        private final Ingredient input;
        private final PotionIngredient reagent;
        private final Holder<Item> output;

        private BrewingRecipeProvider(BiConsumer<Identifier, Recipe<?>> provider, HolderLookup.Provider lookup) {
            this.provider = provider;
            this.potions = lookup.lookupOrThrow(Registries.POTION);
            HolderLookup.RegistryLookup<Item> items = lookup.lookupOrThrow(Registries.ITEM);
            this.input = Ingredient.of(HolderSet.direct(items.getOrThrow(ItemIds.SPLASH_POTION)));
            Ingredient reagentIngredient = Ingredient.of(HolderSet.direct(items.getOrThrow(ItemIds.DRAGON_BREATH)));
            reagentIngredient.itematic$setRemainder(Optional.of(ItemStackTemplates.of(items.getOrThrow(ItemIds.GLASS_BOTTLE))));
            this.reagent = new PotionIngredient(reagentIngredient, Optional.empty());
            this.output = items.getOrThrow(ItemIds.LINGERING_POTION);
        }

        private void configure() {
            this.potions.listElements()
                .filter(potion -> !POTIONS_WITHOUT_RECIPE.contains(potion))
                .forEach(this::addTransform);
        }

        private void addTransform(Holder<Potion> potion) {
            PotionIngredient input = new PotionIngredient(
                this.input,
                Optional.of(PotionsPredicate.ofPotion(potion))
            );
            ItemStackTemplate output = ItemStackTemplates.of(
                this.output,
                DataComponentPatch.builder()
                    .set(DataComponents.POTION_CONTENTS, new PotionContents(potion))
                    .build()
            );
            this.provider.accept(
                BrewingRecipeBuilderAccessor.create(input, this.reagent, output)
                    .defaultId()
                    .identifier(),
                new BrewingRecipe(input, this.reagent, output)
            );
        }
    }
}
