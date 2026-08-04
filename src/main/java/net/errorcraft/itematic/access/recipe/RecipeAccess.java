package net.errorcraft.itematic.access.recipe;

import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;

public interface RecipeAccess {
    default PlacementInfo itematic$ingredientPlacement(HolderGetter<Item> items) {
        return null;
    }
    default List<RecipeDisplay> itematic$displays(HolderGetter<Item> items) {
        return null;
    }
}
