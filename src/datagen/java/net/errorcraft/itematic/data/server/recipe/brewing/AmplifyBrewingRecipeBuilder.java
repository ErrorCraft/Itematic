package net.errorcraft.itematic.data.server.recipe.brewing;

import net.errorcraft.itematic.recipe.brewing.AmplifyBrewingRecipe;
import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;

public class AmplifyBrewingRecipeBuilder extends BrewingRecipeBuilder<Item> {
    public AmplifyBrewingRecipeBuilder(RegistryEntry<Item> base, Ingredient addition, RegistryEntry<Item> result, RegistryEntryList<Item> conditionItems, Identifier name) {
        super(base, addition, result, conditionItems, name);
    }

    @Override
    protected BrewingRecipe<Item> createRecipe() {
        return new AmplifyBrewingRecipe(
            "",
            this.base,
            this.addition,
            this.result
        );
    }
}
