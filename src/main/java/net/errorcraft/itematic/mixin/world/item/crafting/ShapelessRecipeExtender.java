package net.errorcraft.itematic.mixin.world.item.crafting;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

@Mixin(ShapelessRecipe.class)
public abstract class ShapelessRecipeExtender implements CraftingRecipe {
    @Shadow
    @Final
    private ItemStackTemplate result;

    @Shadow
    @Final
    private List<Ingredient> ingredients;

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        IntSet foundInputs = new IntOpenHashSet();
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (Ingredient ingredient : this.ingredients) {
            Optional<ItemStackTemplate> remainder = ingredient.itematic$remainder();
            if (remainder.isEmpty()) {
                continue;
            }

            for (int i = 0; i < input.size(); i++) {
                if (foundInputs.contains(i)) {
                    continue;
                }

                if (!ingredient.test(input.getItem(i))) {
                    continue;
                }

                remainders.set(i, remainder.get().create());
                foundInputs.add(i);
                break;
            }
        }

        return remainders;
    }

    @Override
    public List<RecipeDisplay> itematic$display(HolderGetter<Item> items) {
        return List.of(
            new ShapelessCraftingRecipeDisplay(
                this.ingredients.stream().map(Ingredient::display).toList(),
                new SlotDisplay.ItemStackSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemIds.CRAFTING_TABLE))
            )
        );
    }
}
