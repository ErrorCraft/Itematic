package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SmeltingRecipe.class)
public abstract class SmeltingRecipeExtender extends AbstractCookingRecipeExtender {
    public SmeltingRecipeExtender(CommonInfo commonInfo, Ingredient input, ItemStackTemplate result) {
        super(commonInfo, input, result);
    }

    @Override
    protected ResourceKey<Item> cookerItemId() {
        return ItemIds.FURNACE;
    }
}
