package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.gui.screen.recipebook.AbstractFurnaceRecipeBookScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractFurnaceRecipeBookScreen.class)
public class AbstractFurnaceRecipeBookScreenExtender extends RecipeBookWidget {
    @Shadow
    @Nullable
    private Ingredient fuels;

    @Override
    public void initializeRecipeSpecific(World world) {
        this.fuels = Ingredient.ofStacks(world.itematic$getItemAccess()
            .streamEntries()
            .filter(reference -> reference.value().itematic$hasComponent(ItemComponentTypes.FUEL))
            .map(ItemStack::new)
        );
    }
}
