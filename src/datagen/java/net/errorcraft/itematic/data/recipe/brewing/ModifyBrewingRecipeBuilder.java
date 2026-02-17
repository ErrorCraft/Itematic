package net.errorcraft.itematic.data.recipe.brewing;

import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.errorcraft.itematic.recipe.brewing.ModifyBrewingRecipe;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;

public class ModifyBrewingRecipeBuilder extends BrewingRecipeBuilder<Potion> {
    public ModifyBrewingRecipeBuilder(RegistryEntry<Potion> base, RegistryEntryList<Item> addition, RegistryEntry<Potion> result, Identifier name) {
        super(base, addition, result, name);
    }

    @Override
    protected BrewingRecipe<Potion> createRecipe() {
        return new ModifyBrewingRecipe(
            "",
            this.base,
            this.addition(),
            this.result
        );
    }
}
