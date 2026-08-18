package net.errorcraft.itematic.mixin.world.item.crafting;

import net.errorcraft.itematic.mixin.world.level.block.entity.PotDecorationsAccessor;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.DecoratedPotRecipe;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(DecoratedPotRecipe.class)
public class DecoratedPotRecipeExtender {
    @Redirect(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
        )
    )
    private boolean isDecoratedPotIngredientsCheckItemBehavior(ItemStack instance, TagKey<Item> tag) {
        return !instance.isEmpty() && instance.itematic$hasBehavior(ItemBehaviorType.DECORATED_POT_PATTERN);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a holder for data-driven items.
     */
    @Overwrite
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack stack = registries.lookupOrThrow(Registries.ITEM)
            .get(ItemIds.DECORATED_POT)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
        if (stack.isEmpty()) {
            return stack;
        }

        PotDecorations potDecorations = PotDecorationsAccessor.create(
            Optional.of(input.getItem(1).getItemHolder()),
            Optional.of(input.getItem(3).getItemHolder()),
            Optional.of(input.getItem(5).getItemHolder()),
            Optional.of(input.getItem(7).getItemHolder())
        );
        stack.set(DataComponents.POT_DECORATIONS, potDecorations);
        return stack;
    }
}
