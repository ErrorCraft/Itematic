package net.errorcraft.itematic.mixin.recipe;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ShapelessRecipe.class)
public abstract class ShapelessRecipeExtender implements CraftingRecipe, RecipeAccess {
    @Shadow
    @Final
    ItemStack result;

    @Shadow
    @Final
    List<Ingredient> ingredients;

    @Override
    public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput input) {
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

    @Override
    public List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        return List.of(
            new ShapelessCraftingRecipeDisplay(
                this.ingredients.stream().map(Ingredient::toDisplay).toList(),
                new SlotDisplay.StackSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.CRAFTING_TABLE))
            )
        );
    }
}
