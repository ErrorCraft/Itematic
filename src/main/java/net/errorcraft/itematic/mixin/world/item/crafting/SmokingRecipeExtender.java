package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmokingRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SmokingRecipe.class)
public abstract class SmokingRecipeExtender extends AbstractCookingRecipeExtender {
    public SmokingRecipeExtender(CommonInfo commonInfo, Ingredient input, ItemStackTemplate result) {
        super(commonInfo, input, result);
    }

    @Override
    protected ResourceKey<Item> cookerItemId() {
        return ItemIds.SMOKER;
    }
}
