package net.errorcraft.itematic.access.world.item.crafting;

import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;

public interface RecipeAccess {
    default PlacementInfo itematic$placementInfo(HolderGetter<Item> items) {
        throw new AssertionError("Implemented via mixin");
    }
    default List<RecipeDisplay> itematic$display(HolderGetter<Item> items) {
        throw new AssertionError("Implemented via mixin");
    }
}
