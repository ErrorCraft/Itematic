package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.block.entity.SherdsAccessor;
import net.minecraft.block.entity.Sherds;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingDecoratedPotRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(CraftingDecoratedPotRecipe.class)
public class CraftingDecoratedPotRecipeExtender {
    @Redirect(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
        )
    )
    private boolean isInForDecoratedPotIngredientsUseItemComponentCheck(ItemStack instance, TagKey<Item> tag) {
        return !instance.isEmpty() && instance.itematic$hasBehavior(ItemComponentTypes.DECORATED_POT_PATTERN);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a registry entry for data-driven items.
     */
    @Overwrite
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack stack = lookup.getOrThrow(RegistryKeys.ITEM)
            .getOptional(ItemKeys.DECORATED_POT)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
        if (stack.isEmpty()) {
            return stack;
        }

        Sherds sherds = SherdsAccessor.create(
            Optional.of(input.getStackInSlot(1).getRegistryEntry()),
            Optional.of(input.getStackInSlot(3).getRegistryEntry()),
            Optional.of(input.getStackInSlot(5).getRegistryEntry()),
            Optional.of(input.getStackInSlot(7).getRegistryEntry())
        );
        stack.set(DataComponentTypes.POT_DECORATIONS, sherds);
        return stack;
    }
}
