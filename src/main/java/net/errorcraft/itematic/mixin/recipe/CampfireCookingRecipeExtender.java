package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CampfireCookingRecipe.class)
public abstract class CampfireCookingRecipeExtender extends AbstractCookingRecipeExtender implements RecipeAccess {
    public CampfireCookingRecipeExtender(String group, Ingredient ingredient, ItemStack result) {
        super(group, ingredient, result);
    }

    @Override
    protected ResourceKey<Item> cookerItemKey() {
        return ItemIds.CAMPFIRE;
    }
}
