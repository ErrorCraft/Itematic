package net.errorcraft.itematic.data.server;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModifiedRecipeProvider extends FabricCodecDataProvider<Recipe<?>> {
    public ModifiedRecipeProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, PackOutput.Target.DATA_PACK, "recipe", Recipe.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Recipe<?>> provider, HolderLookup.Provider lookup) {
        HolderLookup.RegistryLookup<Item> items = lookup.lookupOrThrow(Registries.ITEM);
        provider.accept(
            Identifier.withDefaultNamespace("honey_block"),
            shapedRecipe(RecipeCategory.FOOD, items.getOrThrow(ItemIds.HONEY_BLOCK))
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
            shapedRecipe(RecipeCategory.FOOD, items.getOrThrow(ItemIds.CAKE))
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
        return new ShapelessRecipeBuilder(new ItemStack(result, count), category);
    }

    private static ShapedRecipeBuilder shapedRecipe(RecipeCategory category, Holder<Item> result) {
        return new ShapedRecipeBuilder(new ItemStack(result), category);
    }

    private static class ShapelessRecipeBuilder {
        private final ItemStack result;
        private final RecipeCategory category;
        private final List<Ingredient> inputs = new ArrayList<>();

        private ShapelessRecipeBuilder(ItemStack result, RecipeCategory category) {
            this.result = result;
            this.category = category;
        }

        public ShapelessRecipe build() {
            return new ShapelessRecipe(
                "",
                RecipeBuilder.determineBookCategory(this.category),
                this.result,
                this.inputs
            );
        }

        public ShapelessRecipeBuilder input(Holder<Item> input, int count, Holder<Item> remainder) {
            for (int i = 0; i < count; i++) {
                Ingredient ingredient = Ingredient.of(HolderSet.direct(input));
                ingredient.itematic$setRemainder(Optional.of(new ItemStack(remainder)));
                this.inputs.add(ingredient);
            }

            return this;
        }
    }

    private static class ShapedRecipeBuilder {
        private final ItemStack result;
        private final RecipeCategory category;
        private final Char2ObjectMap<Ingredient> inputs = new Char2ObjectOpenHashMap<>();
        private final List<String> pattern = new ArrayList<>();

        private ShapedRecipeBuilder(ItemStack result, RecipeCategory category) {
            this.result = result;
            this.category = category;
        }

        public ShapedRecipe build() {
            return new ShapedRecipe(
                "",
                RecipeBuilder.determineBookCategory(this.category),
                ShapedRecipePattern.of(this.inputs, this.pattern),
                this.result,
                true
            );
        }

        public ShapedRecipeBuilder input(char key, Holder<Item> input) {
            this.inputs.put(key, Ingredient.of(HolderSet.direct(input)));
            return this;
        }

        public ShapedRecipeBuilder input(char key, Holder<Item> input, Holder<Item> remainder) {
            Ingredient ingredient = Ingredient.of(HolderSet.direct(input));
            ingredient.itematic$setRemainder(Optional.of(new ItemStack(remainder)));
            this.inputs.put(key, ingredient);
            return this;
        }

        public ShapedRecipeBuilder pattern(String pattern) {
            this.pattern.add(pattern);
            return this;
        }
    }
}
