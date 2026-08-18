package net.errorcraft.itematic.data.recipe.brewing;

import net.errorcraft.itematic.world.item.crafting.BrewingRecipe;
import net.errorcraft.itematic.world.item.crafting.ModifyBrewingRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;

public class ModifyBrewingRecipeBuilder extends BrewingRecipeBuilder<Potion> {
    public ModifyBrewingRecipeBuilder(Holder<Potion> base, HolderSet<Item> reagent, Holder<Potion> result, Identifier name) {
        super(base, reagent, result, name);
    }

    @Override
    protected BrewingRecipe<Potion> createRecipe() {
        return new ModifyBrewingRecipe(
            this.base,
            this.reagent(),
            this.result
        );
    }
}
