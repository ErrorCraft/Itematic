package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingTrimRecipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SmithingRecipeDisplay;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(SmithingTrimRecipe.class)
public class SmithingTrimRecipeExtender implements RecipeAccess {
    @Shadow
    @Final
    Ingredient template;

    @Shadow
    @Final
    Ingredient base;

    @Shadow
    @Final
    Ingredient addition;

    @Shadow
    @Final
    RegistryEntry<ArmorTrimPattern> pattern;

    @Override
    public List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        SlotDisplay base = this.base.toDisplay();
        SlotDisplay material = this.addition.toDisplay();
        SlotDisplay pattern = this.template.toDisplay();
        return List.of(
            new SmithingRecipeDisplay(
                pattern,
                base,
                material,
                new SlotDisplay.SmithingTrimSlotDisplay(base, material, this.pattern),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.SMITHING_TABLE))
            )
        );
    }
}
