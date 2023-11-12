package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.IngredientAccess;
import net.errorcraft.itematic.access.recipe.IngredientEntryAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
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
    public void itematic$initMatchingStacks(Registry<Item> registry) {
        this.matchingStacks = Arrays.stream(this.entries).flatMap(entry -> entry.itematic$getStacks(registry).stream()).distinct().toArray(ItemStack[]::new);
    }

    @Mixin(targets = "net/minecraft/recipe/Ingredient$TagEntry")
    public static class TagEntryExtender implements IngredientEntryAccess {
        @Shadow
        @Final
        private TagKey<Item> tag;

        @Override
        public Collection<ItemStack> itematic$getStacks(Registry<Item> registry) {
            ArrayList<ItemStack> itemStacks = new ArrayList<>();
            for (RegistryEntry<Item> entry : registry.iterateEntries(this.tag)) {
                itemStacks.add(new ItemStack(entry));
            }
            return itemStacks;
        }
    }

    @Mixin(targets = "net/minecraft/recipe/Ingredient$StackEntry")
    public static abstract class StackEntryExtender implements IngredientEntryAccess {
        @Shadow
        public abstract Collection<ItemStack> getStacks();

        @Override
        public Collection<ItemStack> itematic$getStacks(Registry<Item> registry) {
            return this.getStacks();
        }
    }
}
