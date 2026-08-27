package net.errorcraft.itematic.mixin.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.FireworkRocketRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FireworkRocketRecipe.class)
public abstract class FireworkRocketRecipeExtender extends CustomRecipe {
    @Shadow
    @Final
    private Ingredient shell;

    @Shadow
    @Final
    private Ingredient fuel;

    @Shadow
    @Final
    private Ingredient star;

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        boolean foundShell = false;
        int fuelCount = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            final int index = i;
            if (!foundShell && this.shell.test(stack)) {
                foundShell = true;
                this.shell.itematic$remainder()
                    .map(ItemStackTemplate::create)
                    .ifPresent(remainder -> remainders.set(index, remainder));
                continue;
            }

            if (fuelCount <= 3 && this.fuel.test(stack)) {
                fuelCount++;
                this.fuel.itematic$remainder()
                    .map(ItemStackTemplate::create)
                    .ifPresent(remainder -> remainders.set(index, remainder));
                continue;
            }

            this.star.itematic$remainder()
                .map(ItemStackTemplate::create)
                .ifPresent(remainder -> remainders.set(index, remainder));
        }

        return remainders;
    }
}
