package net.errorcraft.itematic.data.recipe.brewing;

import net.errorcraft.itematic.recipe.brewing.AmplifyBrewingRecipe;
import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class AmplifyBrewingRecipeBuilder extends BrewingRecipeBuilder<Item> {
    public AmplifyBrewingRecipeBuilder(Holder<Item> base, HolderSet<Item> reagent, Holder<Item> result, Identifier name) {
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
