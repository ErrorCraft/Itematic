package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.access.world.item.crafting.ShapedRecipePatternAccess;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

@Mixin(ShapedRecipePattern.class)
public abstract class ShapedRecipePatternExtender implements ShapedRecipePatternAccess {
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
    protected abstract boolean matches(CraftingInput input, boolean xFlip);

    @Override
    public NonNullList<ItemStack> itematic$remainder(CraftingInput input) {
        boolean actuallyFlipped = !this.symmetrical && this.matches(input, true);
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int index = actuallyFlipped ?
                    this.width - x - 1 + y * this.width :
                    x + y * this.width;
                this.ingredients.get(index)
                    .flatMap(Ingredient::itematic$remainder)
                    .map(ItemStackTemplate::create)
                    .ifPresent(remainder -> remainders.set(index, remainder));
            }
        }

        return remainders;
    }
}
