package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RawShapedRecipeAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShapedRecipe.class)
public abstract class ShapedRecipeExtender implements CraftingRecipe {
    @Shadow
    @Final
    RawShapedRecipe raw;

    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput input) {
        return ((RawShapedRecipeAccess)(Object) this.raw).itematic$remainder(input);
    }
}
