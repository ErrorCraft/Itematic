package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.block.entity.SherdsUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.block.entity.SherdsAccessor;
import net.minecraft.core.HolderLookup;
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
public class CraftingDecoratedPotRecipeExtender {
    @Redirect(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
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
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider lookup) {
        ItemStack stack = lookup.lookupOrThrow(Registries.ITEM)
            .get(ItemKeys.DECORATED_POT)
            .map(ItemStack::new)
            .orElse(ItemStack.EMPTY);
        if (stack.isEmpty()) {
            return stack;
        }

        PotDecorations sherds = SherdsAccessor.create(
            Optional.of(input.getItem(1).getItemHolder()),
            Optional.of(input.getItem(3).getItemHolder()),
            Optional.of(input.getItem(5).getItemHolder()),
            Optional.of(input.getItem(7).getItemHolder())
        );
        return SherdsUtil.addSherdsToStack(stack, sherds);
    }
}
