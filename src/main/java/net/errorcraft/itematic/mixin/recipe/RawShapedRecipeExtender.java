package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RawShapedRecipeAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RawShapedRecipe.class)
public abstract class RawShapedRecipeExtender implements RawShapedRecipeAccess {
    @Shadow
    @Final
    private int width;

    @Shadow
    @Final
    private int height;

    @Shadow
    @Final
    private boolean symmetrical;

    @Shadow
    @Final
    private DefaultedList<Ingredient> ingredients;

    @Shadow
    protected abstract boolean matches(CraftingRecipeInput input, boolean mirrored);

    @Override
    public DefaultedList<ItemStack> itematic$remainder(CraftingRecipeInput input) {
        boolean actuallyMirrored = !this.symmetrical && this.matches(input, true);
        DefaultedList<ItemStack> remainders = DefaultedList.ofSize(input.getSize(), ItemStack.EMPTY);
        for(int y = 0; y < this.height; y++) {
            for(int x = 0; x < this.width; x++) {
                int index = actuallyMirrored ?
                    this.width - x - 1 + y * this.width :
                    x + y * this.width;
                this.ingredients.get(index)
                    .itematic$remainder()
                    .ifPresent(remainder -> remainders.set(index, remainder.copy()));
            }
        }

        return remainders;
    }
}
