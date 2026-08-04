package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.IngredientAccess;
import net.errorcraft.itematic.access.recipe.RawShapedRecipeAccess;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

@Mixin(ShapedRecipePattern.class)
public abstract class RawShapedRecipeExtender implements RawShapedRecipeAccess {
    @Shadow
    @Final
    private int width;

    @Shadow
    @Final
    private int height;

    @Shadow
    @Final
    private List<Optional<Ingredient>> ingredients;

    @Shadow
    @Final
    private boolean symmetrical;

    @Shadow
    protected abstract boolean matches(CraftingInput input, boolean mirrored);

    @Override
    public NonNullList<ItemStack> itematic$remainder(CraftingInput input) {
        boolean actuallyMirrored = !this.symmetrical && this.matches(input, true);
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int index = actuallyMirrored ?
                    this.width - x - 1 + y * this.width :
                    x + y * this.width;
                this.ingredients.get(index)
                    .flatMap(IngredientAccess::itematic$remainder)
                    .ifPresent(remainder -> remainders.set(index, remainder.copy()));
            }
        }

        return remainders;
    }
}
