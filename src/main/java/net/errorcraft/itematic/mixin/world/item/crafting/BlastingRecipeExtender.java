package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlastingRecipe.class)
public abstract class BlastingRecipeExtender extends AbstractCookingRecipeExtender {
    public BlastingRecipeExtender(CommonInfo commonInfo, Ingredient input, ItemStackTemplate result) {
        super(commonInfo, input, result);
    }

    @Override
    protected ResourceKey<Item> cookerItemId() {
        return ItemIds.BLAST_FURNACE;
    }
}
