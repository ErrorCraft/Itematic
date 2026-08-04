package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Recipe.class)
public interface RecipeExtender extends RecipeAccess {
    @Shadow
    PlacementInfo placementInfo();

    @Shadow
    List<RecipeDisplay> display();

    @Override
    default PlacementInfo itematic$ingredientPlacement(HolderGetter<Item> items) {
        return this.placementInfo();
    }

    @Override
    default List<RecipeDisplay> itematic$displays(HolderGetter<Item> items) {
        return this.display();
    }
}
