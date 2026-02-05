package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.MapExtendingRecipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MapExtendingRecipe.class)
public class MapExtendingRecipeExtender {
    @Unique
    private static final int MAP_SLOT = 4;

    @Redirect(
        method = "<init>",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForMapReturnEmptyStack(ItemConvertible item) {
        return ItemStack.EMPTY;
    }

    @Redirect(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/ShapedRecipe;matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z"
        )
    )
    private boolean matchesUseRegistryKeyCheck(ShapedRecipe instance, CraftingRecipeInput input, World world) {
        for (int i = 0; i < input.getStackCount(); ++i) {
            ItemStack stack = input.getStackInSlot(i);
            if (!isValid(stack, i)) {
                return false;
            }
        }

        return true;
    }

    @Unique
    private static boolean isValid(ItemStack stack, int index) {
        if (index == MAP_SLOT) {
            return stack.itematic$hasBehavior(ItemComponentTypes.MAP_HOLDER);
        }

        return stack.itematic$isOf(ItemKeys.PAPER);
    }
}
