package net.errorcraft.itematic.mixin.world.level.block.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.BrewingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityExtender {
    @ModifyExpressionValue(
        method = "doBrew",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/BrewingRecipe;assemble(Lnet/minecraft/world/item/crafting/BrewingInput;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack storeRemainderIfPresent(ItemStack original, @Local(name = "recipe") Optional<RecipeHolder<BrewingRecipe>> recipe, @Share("reagentRemainder") LocalRef<@Nullable ItemStackTemplate> reagentRemainder) {
        if (reagentRemainder.get() == null) {
            recipe.orElseThrow()
                .value()
                .getReagent()
                .ingredient()
                .itematic$remainder()
                .ifPresent(reagentRemainder::set);
        }

        return original;
    }

    @WrapOperation(
        method = "doBrew",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;getCraftingRemainder()Lnet/minecraft/world/item/ItemStackTemplate;"
        )
    )
    @Nullable
    private static ItemStackTemplate getRemainderFromReagent(Item instance, Operation<ItemStackTemplate> original, @Share("reagentRemainder") LocalRef<@Nullable ItemStackTemplate> reagentRemainder) {
        return reagentRemainder.get();
    }
}
