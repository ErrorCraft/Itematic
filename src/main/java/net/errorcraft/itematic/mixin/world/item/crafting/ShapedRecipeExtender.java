package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Optional;

@Mixin(ShapedRecipe.class)
public abstract class ShapedRecipeExtender implements CraftingRecipe {
    @Shadow
    @Final
    private ShapedRecipePattern pattern;

    @Shadow
    @Final
    private ItemStackTemplate result;

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        return this.pattern.itematic$remainder(input);
    }

    @Override
    public List<RecipeDisplay> itematic$display(HolderGetter<Item> items) {
        return List.of(
            new ShapedCraftingRecipeDisplay(
                this.pattern.width(),
                this.pattern.height(),
                this.pattern.ingredients()
                    .stream()
                    .map(ShapedRecipeExtender::ingredientSlotDisplay)
                    .toList(),
                new SlotDisplay.ItemStackSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemIds.CRAFTING_TABLE))
            )
        );
    }

    @Unique
    private static SlotDisplay ingredientSlotDisplay(Optional<Ingredient> ingredient) {
        return ingredient.map(Ingredient::display)
            .orElse(SlotDisplay.Empty.INSTANCE);
    }
}
