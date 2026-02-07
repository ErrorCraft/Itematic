package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.TransmuteRecipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(TransmuteRecipe.class)
public abstract class TransmuteRecipeExtender implements CraftingRecipe, RecipeAccess {
    @Shadow
    @Final
    Ingredient input;

    @Shadow
    @Final
    Ingredient material;

    @Shadow
    @Final
    RegistryEntry<Item> result;

    @Shadow
    public abstract List<RecipeDisplay> getDisplays();

    @Redirect(
        method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;copyComponentsToNewStack(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack copyComponentsToNewStackUseRegistryEntry(ItemStack instance, ItemConvertible item, int count) {
        return instance.itematic$copyComponentsToNewStack(this.result, count);
    }

    @Override
    public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput input) {
        DefaultedList<ItemStack> remainders = DefaultedList.ofSize(input.size(), ItemStack.EMPTY);
        boolean foundInput = false;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }

            final int index = i;
            if (!foundInput && this.input.test(stack) && !stack.itemMatches(this.result)) {
                foundInput = true;
                this.input.itematic$remainder()
                    .map(ItemStack::copy)
                    .ifPresent(remainder -> remainders.set(index, remainder));
            } else {
                this.material.itematic$remainder()
                    .map(ItemStack::copy)
                    .ifPresent(remainder -> remainders.set(index, remainder));
            }
        }

        return remainders;
    }

    @Override
    public List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        return List.of(
            new ShapelessCraftingRecipeDisplay(
                List.of(
                    this.input.toDisplay(),
                    this.material.toDisplay()
                ),
                new SlotDisplay.ItemSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.CRAFTING_TABLE))
            )
        );
    }
}
