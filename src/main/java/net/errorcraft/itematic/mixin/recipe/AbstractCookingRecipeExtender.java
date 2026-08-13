package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(AbstractCookingRecipe.class)
public abstract class AbstractCookingRecipeExtender extends SingleItemRecipe implements RecipeAccess {
    @Shadow
    @Final
    private float experience;

    @Shadow
    @Final
    private int cookingTime;

    public AbstractCookingRecipeExtender(String group, Ingredient ingredient, ItemStack result) {
        super(group, ingredient, result);
    }

    @Override
    public List<RecipeDisplay> itematic$displays(HolderGetter<Item> items) {
        return List.of(
            new FurnaceRecipeDisplay(
                this.input().display(),
                SlotDisplay.AnyFuel.INSTANCE,
                new SlotDisplay.ItemStackSlotDisplay(this.result()),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(this.cookerItemKey())),
                this.cookingTime,
                this.experience
            )
        );
    }

    @Unique
    protected ResourceKey<Item> cookerItemKey() {
        return ItemIds.FURNACE;
    }
}
