package net.errorcraft.itematic.mixin.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ImbueRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.NormalCraftingRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ImbueRecipe.class)
public abstract class ImbueRecipeExtender extends NormalCraftingRecipe {
    @Shadow
    @Final
    private Ingredient source;

    @Shadow
    @Final
    private Ingredient material;

    protected ImbueRecipeExtender(CommonInfo commonInfo, CraftingBookInfo bookInfo) {
        super(commonInfo, bookInfo);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int y = 0; y < input.height(); y++) {
            for (int x = 0; x < input.width(); x++) {
                int index = x + y * input.width();
                Ingredient ingredient = x == 1 && y == 1
                    ? this.source
                    : this.material;
                ingredient.itematic$remainder()
                    .map(ItemStackTemplate::create)
                    .ifPresent(remainder -> remainders.set(index, remainder));
            }
        }

        return remainders;
    }
}
