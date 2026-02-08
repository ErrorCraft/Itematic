package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingTransformRecipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SmithingRecipeDisplay;
import net.minecraft.registry.RegistryEntryLookup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Optional;

@Mixin(SmithingTransformRecipe.class)
public class SmithingTransformRecipeExtender implements RecipeAccess {
    @Shadow
    @Final
    Optional<Ingredient> template;

    @Shadow
    @Final
    Optional<Ingredient> base;

    @Shadow
    @Final
    Optional<Ingredient> addition;

    @Shadow
    @Final
    ItemStack result;

    @Redirect(
        method = "craft(Lnet/minecraft/recipe/input/SmithingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;copyComponentsToNewStack(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack copyComponentsToNewStackUseRegistryEntry(ItemStack instance, ItemConvertible item, int count) {
        return instance.itematic$copyComponentsToNewStack(this.result.getRegistryEntry(), count);
    }

    @Override
    public List<RecipeDisplay> itematic$displays(RegistryEntryLookup<Item> items) {
        return List.of(
            new SmithingRecipeDisplay(
                Ingredient.toDisplay(this.template),
                Ingredient.toDisplay(this.base),
                Ingredient.toDisplay(this.addition),
                new SlotDisplay.StackSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(items.getOrThrow(ItemKeys.SMITHING_TABLE))
            )
        );
    }
}
