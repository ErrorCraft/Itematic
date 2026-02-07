package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.StonecutterRecipeDisplay;
import net.minecraft.registry.RegistryEntryLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(StonecuttingRecipe.class)
public abstract class StonecuttingRecipeExtender implements RecipeAccess {
    @Shadow
    public abstract SlotDisplay createResultDisplay();

    @Override
    public List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        return List.of(
            new StonecutterRecipeDisplay(
                this.createResultDisplay(),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.STONECUTTER))
            )
        );
    }
}
