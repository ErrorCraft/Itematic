package net.errorcraft.itematic.data.recipe.brewing;

import net.errorcraft.itematic.recipe.brewing.AmplifyBrewingRecipe;
import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;

public class AmplifyBrewingRecipeBuilder extends BrewingRecipeBuilder<Item> {
    public AmplifyBrewingRecipeBuilder(RegistryEntry<Item> base, RegistryEntryList<Item> reagent, RegistryEntry<Item> result, Identifier name) {
        super(base, reagent, result, name);
    }

    @Override
    protected BrewingRecipe<Item> createRecipe() {
        return new AmplifyBrewingRecipe(
            this.base,
            this.reagent(),
            this.result
        );
    }
}
