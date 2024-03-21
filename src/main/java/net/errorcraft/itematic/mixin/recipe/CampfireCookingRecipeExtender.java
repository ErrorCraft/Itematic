package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.registry.RegistryEntryLookup;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CampfireCookingRecipe.class)
public class CampfireCookingRecipeExtender implements RecipeAccess {
    @Override
    public ItemStack itematic$createIcon(RegistryEntryLookup<Item> items) {
        return new ItemStack(items.getOrThrow(ItemKeys.CAMPFIRE));
    }
}
