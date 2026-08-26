package net.errorcraft.itematic.mixin.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.MapExtendingRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MapExtendingRecipe.class)
public abstract class MapExtendingRecipeExtender extends CustomRecipe {
    @Shadow
    @Final
    private ShapedRecipePattern pattern;

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        return this.pattern.itematic$remainder(input);
    }
}
