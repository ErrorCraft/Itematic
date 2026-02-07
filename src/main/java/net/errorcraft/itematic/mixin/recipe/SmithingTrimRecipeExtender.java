package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.recipe.SmithingTrimRecipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SmithingRecipeDisplay;
import net.minecraft.registry.RegistryEntryLookup;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(SmithingTrimRecipe.class)
public class SmithingTrimRecipeExtender implements RecipeAccess {
    @Override
    public List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        return List.of(
            new SmithingRecipeDisplay(
                SlotDisplay.SmithingTrimSlotDisplay.INSTANCE,
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.SMITHING_TABLE))
            )
        );
    }
}
