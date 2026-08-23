package net.errorcraft.itematic.mixin.world.item.crafting;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.TippedArrowRecipe;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TippedArrowRecipe.class)
public class TippedArrowRecipeExtender {
    @Redirect(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 1
        )
    )
    private boolean isArrowCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.ARROW);
    }

    @Redirect(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForTippedArrowUseHolder(ItemLike item, int count, @Local(name = "registries", argsOnly = true) HolderLookup.Provider registries) {
        return registries.lookupOrThrow(Registries.ITEM)
            .get(ItemIds.TIPPED_ARROW)
            .map(itemHolder -> new ItemStack(itemHolder, count))
            .orElse(ItemStack.EMPTY);
    }

    @Redirect(
        method = {
            "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        )
    )
    private boolean isLingeringPotionCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.LINGERING_POTION);
    }
}
