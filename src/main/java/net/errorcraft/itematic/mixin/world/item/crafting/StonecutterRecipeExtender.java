package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.access.world.item.crafting.RecipeAccess;
import net.errorcraft.itematic.references.ItemIds;
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
public abstract class StonecutterRecipeExtender extends SingleItemRecipe implements RecipeAccess {
    public StonecutterRecipeExtender(String group, Ingredient input, ItemStack result) {
        super(group, input, result);
    }

    @Shadow
    public abstract SlotDisplay resultDisplay();

    @Override
    public List<RecipeDisplay> itematic$display(HolderGetter<Item> items) {
        return List.of(
            new StonecutterRecipeDisplay(
                this.input().display(),
                this.resultDisplay(),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemIds.STONECUTTER))
            )
        );
    }
}
