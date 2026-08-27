package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(AbstractCookingRecipe.class)
public abstract class AbstractCookingRecipeExtender extends SingleItemRecipe {
    @Shadow
    @Final
    private float experience;

    @Shadow
    @Final
    private int cookingTime;

    public AbstractCookingRecipeExtender(CommonInfo commonInfo, Ingredient input, ItemStackTemplate result) {
        super(commonInfo, input, result);
    }

    @Override
    public List<RecipeDisplay> itematic$display(HolderGetter<Item> items) {
        return List.of(
            new FurnaceRecipeDisplay(
                this.input().display(),
                SlotDisplay.AnyFuel.INSTANCE,
                new SlotDisplay.ItemStackSlotDisplay(this.result()),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(this.cookerItemId())),
                this.cookingTime,
                this.experience
            )
        );
    }

    @Unique
    protected ResourceKey<Item> cookerItemId() {
        return ItemIds.FURNACE;
    }
}
