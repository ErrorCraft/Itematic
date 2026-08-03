package net.errorcraft.itematic.data.recipe.brewing;

import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;

import java.util.Optional;

public abstract class BrewingRecipeBuilder<T> {
    protected final RegistryEntry<T> base;
    private final RegistryEntryList<Item> reagent;
    protected final RegistryEntry<T> result;
    private RegistryEntry<Item> remainder;
    private final Identifier name;

    protected BrewingRecipeBuilder(RegistryEntry<T> base, RegistryEntryList<Item> reagent, RegistryEntry<T> result, Identifier name) {
        this.base = base;
        this.reagent = reagent;
        this.result = result;
        this.name = name;
    }

    public BrewingRecipeBuilder<T> remainder(RegistryEntry<Item> remainder) {
        this.remainder = remainder;
        return this;
    }

    public void save(RecipeExporter exporter) {
        RegistryKey<Recipe<?>> key = RegistryKey.of(RegistryKeys.RECIPE, this.name);
        Advancement.Builder advancementBuilder = exporter.getAdvancementBuilder()
            .criterion("has_the_recipe", RecipeUnlockedCriterion.create(key))
            .criterion("has_reagent", InventoryChangedCriterion.Conditions.items(
                ItemPredicate.Builder.create()
                    .itematic$items(this.reagent)
            ))
            .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
            .rewards(AdvancementRewards.Builder.recipe(key));
        BrewingRecipe<T> recipe = this.createRecipe();
        exporter.accept(
            key,
            recipe,
            advancementBuilder.build(key.getValue().withPrefixedPath("recipes/brewing/"))
        );
    }

    protected abstract BrewingRecipe<T> createRecipe();

    protected Ingredient reagent() {
        Ingredient reagent = Ingredient.ofTag(this.reagent);
        reagent.itematic$setRemainder(Optional.ofNullable(this.remainder).map(ItemStack::new));
        return reagent;
    }
}
