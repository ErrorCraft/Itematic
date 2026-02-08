package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingTrimRecipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SmithingRecipeDisplay;
import net.minecraft.registry.RegistryEntryLookup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

@Mixin(SmithingTrimRecipe.class)
public class SmithingTrimRecipeExtender implements RecipeAccess {
    @Shadow
    @Final
    Optional<Ingredient> template;

    @Shadow
    @Final
    Optional<Ingredient> base;

    @Shadow
    @Final
    Optional<Ingredient> addition;

    @Override
    public List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        SlotDisplay base = Ingredient.toDisplay(this.base);
        SlotDisplay material = Ingredient.toDisplay(this.addition);
        SlotDisplay pattern = Ingredient.toDisplay(this.template);
        return List.of(
            new SmithingRecipeDisplay(
                pattern,
                base,
                material,
                new SlotDisplay.SmithingTrimSlotDisplay(base, material, pattern),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.SMITHING_TABLE))
            )
        );
    }
}
