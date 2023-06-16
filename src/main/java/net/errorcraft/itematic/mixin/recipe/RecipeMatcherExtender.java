package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
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
        return ItemStackUtil.getRawId(stack.getRegistryEntry());
    }
}
