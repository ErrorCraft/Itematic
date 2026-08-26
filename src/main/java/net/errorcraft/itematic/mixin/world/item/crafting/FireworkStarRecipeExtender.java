package net.errorcraft.itematic.mixin.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.FireworkStarRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(FireworkStarRecipe.class)
public abstract class FireworkStarRecipeExtender extends CustomRecipe {
    @Shadow
    @Final
    private Map<FireworkExplosion.Shape, Ingredient> shapes;

    @Shadow
    @Final
    private Ingredient trail;

    @Shadow
    @Final
    private Ingredient twinkle;

    @Shadow
    @Final
    private Ingredient fuel;

    @Shadow
    @Final
    private Ingredient dye;

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        boolean foundFuel = false;
        boolean foundShape = false;
        boolean foundTrail = false;
        boolean foundTwinkle = false;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            final int index = i;
            if (!foundTwinkle && this.twinkle.test(stack)) {
                foundTwinkle = true;
                this.twinkle.itematic$remainder()
                    .map(ItemStackTemplate::create)
                    .ifPresent(remainder -> remainders.set(index, remainder));
                continue;
            }

            if (!foundTrail && this.trail.test(stack)) {
                foundTrail = true;
                this.trail.itematic$remainder()
                    .map(ItemStackTemplate::create)
                    .ifPresent(remainder -> remainders.set(index, remainder));
                continue;
            }

            if (!foundFuel && this.fuel.test(stack)) {
                foundFuel = true;
                this.fuel.itematic$remainder()
                    .map(ItemStackTemplate::create)
                    .ifPresent(remainder -> remainders.set(index, remainder));
                continue;
            }

            if (this.dye.test(stack) && stack.has(DataComponents.DYE)) {
                this.dye.itematic$remainder()
                    .map(ItemStackTemplate::create)
                    .ifPresent(remainder -> remainders.set(index, remainder));
                continue;
            }

            if (!foundShape) {
                ItemStackTemplate shapeRemainder = this.findShapeRemainder(stack);
                if (shapeRemainder == null) {
                    continue;
                }

                foundShape = true;
                remainders.set(i, shapeRemainder.create());
            }
        }

        return remainders;
    }

    @Unique
    @Nullable
    private ItemStackTemplate findShapeRemainder(ItemStack stack) {
        for (Ingredient ingredient : this.shapes.values()) {
            if (ingredient.test(stack)) {
                return ingredient.itematic$remainder()
                    .orElse(null);
            }
        }

        return null;
    }
}
