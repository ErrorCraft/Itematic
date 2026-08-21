package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.access.world.item.crafting.RecipeAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SmeltingRecipe.class)
public abstract class SmeltingRecipeExtender extends AbstractCookingRecipeExtender implements RecipeAccess {
    public SmeltingRecipeExtender(String group, Ingredient input, ItemStack result) {
        super(group, input, result);
    }

    @Override
    protected ResourceKey<Item> cookerItemId() {
        return ItemIds.FURNACE;
    }
}
