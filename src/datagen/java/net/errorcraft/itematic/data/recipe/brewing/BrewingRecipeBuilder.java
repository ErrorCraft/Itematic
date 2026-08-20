package net.errorcraft.itematic.data.recipe.brewing;

import net.errorcraft.itematic.world.item.crafting.BrewingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public abstract class BrewingRecipeBuilder<T> {
    protected final Holder<T> base;
    private final HolderSet<Item> reagent;
    protected final Holder<T> result;
    @Nullable
    private Holder<Item> remainder;
    private final Identifier name;

    protected BrewingRecipeBuilder(Holder<T> base, HolderSet<Item> reagent, Holder<T> result, Identifier name) {
        this.base = base;
        this.reagent = reagent;
        this.result = result;
        this.name = name;
    }

    public BrewingRecipeBuilder<T> remainder(Holder<Item> remainder) {
        this.remainder = remainder;
        return this;
    }

    public void save(RecipeOutput exporter) {
        ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, this.name);
        Advancement.Builder advancementBuilder = exporter.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
            .addCriterion("has_reagent", InventoryChangeTrigger.TriggerInstance.hasItems(
                ItemPredicate.Builder.item()
                    .itematic$items(this.reagent)
            ))
            .requirements(AdvancementRequirements.Strategy.OR)
            .rewards(AdvancementRewards.Builder.recipe(key));
        BrewingRecipe<T> recipe = this.createRecipe();
        exporter.accept(
            key,
            recipe,
            advancementBuilder.build(key.identifier().withPrefix("recipes/brewing/"))
        );
    }

    protected abstract BrewingRecipe<T> createRecipe();

    protected Ingredient reagent() {
        Ingredient reagent = Ingredient.of(this.reagent);
        reagent.itematic$setRemainder(Optional.ofNullable(this.remainder).map(ItemStack::new));
        return reagent;
    }
}
