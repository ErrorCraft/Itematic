package net.errorcraft.itematic.mixin.data.recipes;

import net.minecraft.data.recipes.BrewingRecipeBuilder;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.PotionIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BrewingRecipeBuilder.class)
public interface BrewingRecipeBuilderAccessor {
    @Invoker("<init>")
    static BrewingRecipeBuilder create(final PotionIngredient input, final PotionIngredient reagent, final ItemStackTemplate output) {
        throw new UnsupportedOperationException();
    }
}
