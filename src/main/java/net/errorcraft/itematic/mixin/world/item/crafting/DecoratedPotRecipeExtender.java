package net.errorcraft.itematic.mixin.world.item.crafting;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.mixin.world.level.block.entity.PotDecorationsAccessor;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.DecoratedPotRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(DecoratedPotRecipe.class)
public abstract class DecoratedPotRecipeExtender extends CustomRecipe {
    @Shadow
    @Final
    private Ingredient backPattern;

    @Shadow
    @Final
    private Ingredient leftPattern;

    @Shadow
    @Final
    private Ingredient rightPattern;

    @Shadow
    @Final
    private Ingredient frontPattern;

    @Shadow
    private static ItemStack back(CraftingInput input) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static ItemStack left(CraftingInput input) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static ItemStack right(CraftingInput input) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static ItemStack front(CraftingInput input) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @WrapOperation(
        method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/level/block/entity/PotDecorations;"
        )
    )
    public PotDecorations useHolders(Item back, Item left, Item right, Item front, Operation<PotDecorations> original, CraftingInput input) {
        return PotDecorationsAccessor.create(
            Optional.of(back(input).typeHolder()),
            Optional.of(left(input).typeHolder()),
            Optional.of(right(input).typeHolder()),
            Optional.of(front(input).typeHolder())
        );
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        this.backPattern.itematic$remainder()
            .map(ItemStackTemplate::create)
            .ifPresent(remainder -> remainders.set(1, remainder));
        this.leftPattern.itematic$remainder()
            .map(ItemStackTemplate::create)
            .ifPresent(remainder -> remainders.set(3, remainder));
        this.rightPattern.itematic$remainder()
            .map(ItemStackTemplate::create)
            .ifPresent(remainder -> remainders.set(5, remainder));
        this.frontPattern.itematic$remainder()
            .map(ItemStackTemplate::create)
            .ifPresent(remainder -> remainders.set(7, remainder));
        return remainders;
    }
}
