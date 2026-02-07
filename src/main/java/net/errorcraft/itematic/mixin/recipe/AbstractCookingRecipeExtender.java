package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SingleStackRecipe;
import net.minecraft.recipe.display.FurnaceRecipeDisplay;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(AbstractCookingRecipe.class)
public abstract class AbstractCookingRecipeExtender extends SingleStackRecipe implements RecipeAccess {
    public AbstractCookingRecipeExtender(String group, Ingredient ingredient, ItemStack result) {
        super(group, ingredient, result);
    }

    @Override
    public List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        return List.of(
            new FurnaceRecipeDisplay(
                this.ingredient().toDisplay(),
                SlotDisplay.AnyFuelSlotDisplay.INSTANCE,
                new SlotDisplay.StackSlotDisplay(this.result()),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(this.cookerItemKey()))
            )
        );
    }

    @Unique
    protected RegistryKey<Item> cookerItemKey() {
        return ItemKeys.FURNACE;
    }
}
