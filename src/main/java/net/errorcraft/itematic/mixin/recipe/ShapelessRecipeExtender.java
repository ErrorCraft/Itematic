package net.errorcraft.itematic.mixin.recipe;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ShapelessRecipe.class)
public abstract class ShapelessRecipeExtender implements CraftingRecipe {
    @Shadow
    @Final
    List<Ingredient> ingredients;

    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput input) {
        IntSet foundInputs = new IntOpenHashSet();
        DefaultedList<ItemStack> remainders = DefaultedList.ofSize(input.size(), ItemStack.EMPTY);
        for (Ingredient ingredient : this.ingredients) {
            if (ingredient.itematic$remainder().isEmpty()) {
                continue;
            }

            for (int i = 0; i < input.size(); i++) {
                if (foundInputs.contains(i)) {
                    continue;
                }

                if (!ingredient.test(input.getStackInSlot(i))) {
                    continue;
                }

                remainders.set(i, ingredient.itematic$remainder().get().copy());
                foundInputs.add(i);
                break;
            }
        }

        return remainders;
    }
}
