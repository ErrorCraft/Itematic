package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.IngredientAccess;
import net.errorcraft.itematic.access.recipe.IngredientEntryAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Mixin(Ingredient.class)
public class IngredientExtender implements IngredientAccess {
    @Shadow
    @Final
    private Ingredient.Entry[] entries;

    @Shadow
    @Nullable
    private ItemStack[] matchingStacks;

    @Override
    public void itematic$initMatchingStacks(RegistryWrapper.WrapperLookup lookup) {
        this.matchingStacks = Arrays.stream(this.entries)
            .flatMap(entry -> entry.itematic$getStacks(lookup).stream())
            .distinct()
            .toArray(ItemStack[]::new);
    }

    @Mixin(targets = "net/minecraft/recipe/Ingredient$TagEntry")
    public static class TagEntryExtender implements IngredientEntryAccess {
        @Shadow
        @Final
        private TagKey<Item> tag;

        @Override
        public Collection<ItemStack> itematic$getStacks(RegistryWrapper.WrapperLookup lookup) {
            ArrayList<ItemStack> stacks = new ArrayList<>();
            lookup.getWrapperOrThrow(RegistryKeys.ITEM)
                .getOptional(this.tag)
                .ifPresent(entries -> entries.forEach(entry -> stacks.add(new ItemStack(entry))));
            return stacks;
        }
    }

    @Mixin(targets = "net/minecraft/recipe/Ingredient$StackEntry")
    public static abstract class StackEntryExtender implements IngredientEntryAccess {
        @Shadow
        public abstract Collection<ItemStack> getStacks();

        @Override
        public Collection<ItemStack> itematic$getStacks(RegistryWrapper.WrapperLookup lookup) {
            return this.getStacks();
        }
    }
}
