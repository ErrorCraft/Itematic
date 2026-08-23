package net.errorcraft.itematic.mixin.world.item.crafting;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.FireworkRocketRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(FireworkRocketRecipe.class)
public class FireworkRocketRecipeExtender {
    @Redirect(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z",
            ordinal = 0
        )
    )
    private boolean testPaperCheckId(Ingredient instance, ItemStack input) {
        return input.is(ItemIds.PAPER);
    }

    @Redirect(
        method = {
            "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/crafting/FireworkRocketRecipe;GUNPOWDER_INGREDIENT:Lnet/minecraft/world/item/crafting/Ingredient;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean testGunpowderCheckId(Ingredient instance, ItemStack input) {
        return input.is(ItemIds.GUNPOWDER);
    }

    @Redirect(
        method = {
            "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/crafting/FireworkRocketRecipe;STAR_INGREDIENT:Lnet/minecraft/world/item/crafting/Ingredient;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean testFireworkStarCheckId(Ingredient instance, ItemStack input) {
        return input.is(ItemIds.FIREWORK_STAR);
    }

    @Redirect(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;I)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireworkRocketUseHolder(ItemLike item, int count, @Local(name = "registries", argsOnly = true) HolderLookup.Provider registries) {
        return registries.lookupOrThrow(Registries.ITEM)
            .get(ItemIds.FIREWORK_ROCKET)
            .map(itemHolder -> new ItemStack(itemHolder, count))
            .orElse(ItemStack.EMPTY);
    }
}
