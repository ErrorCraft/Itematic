package net.errorcraft.itematic.data.server.recipe.brewing;

import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.errorcraft.itematic.recipe.brewing.ModifyBrewingRecipe;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;

public class ModifyBrewingRecipeBuilder extends BrewingRecipeBuilder<Potion> {
    public ModifyBrewingRecipeBuilder(RegistryEntry<Potion> base, Ingredient reagent, RegistryEntry<Potion> result, RegistryEntryList<Item> conditionItems, Identifier name) {
        super(base, reagent, result, conditionItems, name);
    }

    @Override
    protected BrewingRecipe<Potion> createRecipe() {
        return new ModifyBrewingRecipe(
            this.base,
            this.reagent,
            this.result
        );
    }
}
