package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(StonecutterRecipe.class)
public abstract class StonecuttingRecipeExtender extends SingleItemRecipe implements RecipeAccess {
    public StonecuttingRecipeExtender(String group, Ingredient ingredient, ItemStack result) {
        super(group, ingredient, result);
    }

    @Shadow
    public abstract SlotDisplay resultDisplay();

    @Override
    public List<RecipeDisplay> itematic$displays(HolderGetter<Item> items) {
        return List.of(
            new StonecutterRecipeDisplay(
                this.input().display(),
                this.resultDisplay(),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.STONECUTTER))
            )
        );
    }
}
