package net.errorcraft.itematic.data.server;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModifiedRecipeProvider extends FabricCodecDataProvider<Recipe<?>> {
    public ModifiedRecipeProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK, "recipe", Recipe.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Recipe<?>> provider, RegistryWrapper.WrapperLookup lookup) {
        RegistryWrapper.Impl<Item> items = lookup.getOrThrow(RegistryKeys.ITEM);
        provider.accept(
            Identifier.ofVanilla("honey_block"),
            shapedRecipe(RecipeCategory.FOOD, items.getOrThrow(ItemKeys.HONEY_BLOCK))
                .input('#', items.getOrThrow(ItemKeys.HONEY_BOTTLE), items.getOrThrow(ItemKeys.GLASS_BOTTLE))
                .pattern("##")
                .pattern("##")
                .build()
        );
        provider.accept(
            Identifier.ofVanilla("sugar_from_honey_bottle"),
            shapelessRecipe(RecipeCategory.MISC, items.getOrThrow(ItemKeys.SUGAR), 3)
                .input(items.getOrThrow(ItemKeys.HONEY_BOTTLE), 1, items.getOrThrow(ItemKeys.GLASS_BOTTLE))
                .build()
        );
        provider.accept(
            Identifier.ofVanilla("cake"),
            shapedRecipe(RecipeCategory.FOOD, items.getOrThrow(ItemKeys.CAKE))
                .input('A', items.getOrThrow(ItemKeys.MILK_BUCKET), items.getOrThrow(ItemKeys.BUCKET))
                .input('B', items.getOrThrow(ItemKeys.SUGAR))
                .input('C', items.getOrThrow(ItemKeys.WHEAT))
                .input('E', items.getOrThrow(ItemKeys.EGG))
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

    private static ShapelessRecipeBuilder shapelessRecipe(RecipeCategory category, RegistryEntry<Item> result, int count) {
        return new ShapelessRecipeBuilder(new ItemStack(result, count), category);
    }

    private static ShapedRecipeBuilder shapedRecipe(RecipeCategory category, RegistryEntry<Item> result) {
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
                CraftingRecipeJsonBuilder.toCraftingCategory(this.category),
                this.result,
                this.inputs
            );
        }

        public ShapelessRecipeBuilder input(RegistryEntry<Item> input, int count, RegistryEntry<Item> remainder) {
            for (int i = 0; i < count; i++) {
                Ingredient ingredient = Ingredient.fromTag(RegistryEntryList.of(input));
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
                CraftingRecipeJsonBuilder.toCraftingCategory(this.category),
                RawShapedRecipe.create(this.inputs, this.pattern),
                this.result,
                true
            );
        }

        public ShapedRecipeBuilder input(char key, RegistryEntry<Item> input) {
            this.inputs.put(key, Ingredient.fromTag(RegistryEntryList.of(input)));
            return this;
        }

        public ShapedRecipeBuilder input(char key, RegistryEntry<Item> input, RegistryEntry<Item> remainder) {
            Ingredient ingredient = Ingredient.fromTag(RegistryEntryList.of(input));
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
