package net.errorcraft.itematic.mixin.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RecipeMatcher.class)
public class RecipeMatcherExtender {
    /**
     * @author ErrorCraft
     * @reason Uses a registry entry for data-driven items.
     */
    @Overwrite
    public static int getItemId(ItemStack stack) {
        RegistryEntry<Item> entry = stack.getRegistryEntry();
        if (entry == null) {
            return -1;
        }
        return entry.itematic$rawId();
    }
}
