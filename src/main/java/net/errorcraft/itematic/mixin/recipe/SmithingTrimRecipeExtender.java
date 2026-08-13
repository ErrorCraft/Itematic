package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
import net.minecraft.world.item.equipment.trim.TrimPattern;
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
    Holder<TrimPattern> pattern;

    @Override
    public List<RecipeDisplay> itematic$displays(HolderGetter<Item> items) {
        SlotDisplay base = this.base.display();
        SlotDisplay material = this.addition.display();
        SlotDisplay pattern = this.template.display();
        return List.of(
            new SmithingRecipeDisplay(
                pattern,
                base,
                material,
                new SlotDisplay.SmithingTrimDemoSlotDisplay(base, material, this.pattern),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemIds.SMITHING_TABLE))
            )
        );
    }
}
