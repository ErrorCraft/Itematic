package net.errorcraft.itematic.data.server.recipe.brewing;

import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;

import java.util.Optional;

public abstract class BrewingRecipeBuilder<T> {
    protected final RegistryEntry<T> base;
    protected final Ingredient addition;
    protected final RegistryEntry<T> result;
    private final RegistryEntryList<Item> conditionItems;
    private final Identifier name;

    protected BrewingRecipeBuilder(RegistryEntry<T> base, Ingredient addition, RegistryEntry<T> result, RegistryEntryList<Item> conditionItems, Identifier name) {
        this.base = base;
        this.addition = addition;
        this.result = result;
        this.conditionItems = conditionItems;
        this.name = name;
    }

    public BrewingRecipeBuilder<T> remainder(RegistryEntry<Item> remainder) {
        this.addition.itematic$setRemainder(Optional.of(new ItemStack(remainder)));
        return this;
    }

    public void save(RecipeExporter exporter) {
        Advancement.Builder advancementBuilder = exporter.getAdvancementBuilder()
            .criterion("has_the_recipe", RecipeUnlockedCriterion.create(this.name))
            .criterion("has_addition", InventoryChangedCriterion.Conditions.items(
                ItemPredicate.Builder.create()
                    .itematic$items(this.conditionItems)
            ))
            .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
            .rewards(AdvancementRewards.Builder.recipe(this.name));
        BrewingRecipe<T> recipe = this.createRecipe();
        exporter.accept(
            this.name,
            recipe,
            advancementBuilder.build(this.name.withPrefixedPath("recipes/brewing/"))
        );
    }

    protected abstract BrewingRecipe<T> createRecipe();
}
