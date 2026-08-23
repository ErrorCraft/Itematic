package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.MapExtendingRecipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
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
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForMapUseEmptyStack(ItemLike item) {
        return ItemStack.EMPTY;
    }

    @Redirect(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/ShapedRecipe;matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z"
        )
    )
    private boolean matchesCheckIdOrBehavior(ShapedRecipe instance, CraftingInput input, Level level) {
        for (int i = 0; i < input.ingredientCount(); i++) {
            ItemStack stack = input.getItem(i);
            if (!isValid(stack, i)) {
                return false;
            }
        }

        return true;
    }

    @Unique
    private static boolean isValid(ItemStack stack, int index) {
        if (index == MAP_SLOT) {
            return stack.itematic$hasBehavior(ItemBehaviorType.MAP_HOLDER);
        }

        return stack.is(ItemIds.PAPER);
    }
}
