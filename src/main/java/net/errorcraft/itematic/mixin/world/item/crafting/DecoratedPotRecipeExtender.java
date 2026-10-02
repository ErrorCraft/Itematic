package net.errorcraft.itematic.mixin.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.DecoratedPotRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
