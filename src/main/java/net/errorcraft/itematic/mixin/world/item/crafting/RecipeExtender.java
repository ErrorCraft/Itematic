package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.access.world.item.crafting.RecipeAccess;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Recipe.class)
public interface RecipeExtender extends RecipeAccess {
    @Shadow
    List<RecipeDisplay> display();

    @Override
    default List<RecipeDisplay> itematic$display(HolderGetter<Item> items) {
        return this.display();
    }
}
