package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SuspiciousStewRecipe;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(SuspiciousStewRecipe.class)
public class SuspiciousStewRecipeExtender {
    @Redirect(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/block/Blocks;BROWN_MUSHROOM:Lnet/minecraft/block/Block;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForBrownMushroomUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BROWN_MUSHROOM);
    }

    @Redirect(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/block/Blocks;RED_MUSHROOM:Lnet/minecraft/block/Block;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForRedMushroomUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.RED_MUSHROOM);
    }

    @Redirect(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
        )
    )
    private boolean isInForSmallFlowersUseItemComponentCheck(ItemStack instance, TagKey<Item> tag) {
        return instance.itematic$hasComponent(ItemComponentTypes.SUSPICIOUS_EFFECT_INGREDIENT);
    }

    @Redirect(
        method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;BOWL:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForBowlUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BOWL);
    }

    @Redirect(
        method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/SuspiciousStewIngredient;of(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/block/SuspiciousStewIngredient;"
        )
    )
    private SuspiciousStewIngredient suspiciousStewEffectsUseItemComponent(ItemConvertible item, @Local(ordinal = 1) ItemStack inputStack) {
        return inputStack.itematic$getComponent(ItemComponentTypes.SUSPICIOUS_EFFECT_INGREDIENT)
            .orElse(null);
    }

    @Redirect(
        method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForSuspiciousStewUseRegistryEntry(ItemConvertible item, int count, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        return lookup.getWrapperOrThrow(RegistryKeys.ITEM)
            .getOptional(ItemKeys.SUSPICIOUS_STEW)
            .map(entry -> new ItemStack(entry, count))
            .orElse(ItemStack.EMPTY);
    }
}
