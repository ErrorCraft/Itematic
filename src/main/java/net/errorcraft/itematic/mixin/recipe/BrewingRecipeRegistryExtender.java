package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.recipe.BrewingRecipeRegistryAccess;
import net.errorcraft.itematic.access.recipe.BrewingRecipeRegistryBuilderAccess;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.recipe.BrewingRecipe;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryExtender implements BrewingRecipeRegistryAccess {
    @Unique
    private List<BrewingRecipe<RegistryKey<Item>>> itemRecipes;

    @Unique
    private List<BrewingRecipe<RegistryEntry<Potion>>> potionRecipes;

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    private boolean isPotionType(ItemStack stack) {
        return stack.itematic$hasComponent(ItemComponentTypes.POTION_HOLDER);
    }

    /**
     * @author ErrorCraft
     * @reason Uses item keys instead of direct items.
     */
    @Overwrite
    public boolean isItemRecipeIngredient(ItemStack stack) {
        for (BrewingRecipe<RegistryKey<Item>> recipe : this.itemRecipes) {
            if (stack.itematic$isOf(recipe.ingredient())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @author ErrorCraft
     * @reason Uses item keys instead of direct items.
     */
    @Overwrite
    public boolean hasItemRecipe(ItemStack input, ItemStack ingredient) {
        for (BrewingRecipe<RegistryKey<Item>> recipe : this.itemRecipes) {
            if (input.itematic$isOf(recipe.input()) && ingredient.itematic$isOf(recipe.ingredient())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @author ErrorCraft
     * @reason Uses item keys instead of direct items.
     */
    @Overwrite
    public boolean isPotionRecipeIngredient(ItemStack stack) {
        for (BrewingRecipe<RegistryEntry<Potion>> recipe : this.potionRecipes) {
            if (stack.itematic$isOf(recipe.ingredient())) {
                return true;
            }
        }

        return false;
    }

    @Definition(
        id = "iterator",
        method = "Ljava/util/List;iterator()Ljava/util/Iterator;"
    )
    @Expression("? = ?.iterator()")
    @Inject(
        method = "hasPotionRecipe",
        at = @At(
            value = "MIXINEXTRAS:EXPRESSION",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void hasPotionRecipeUseItemKeys(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> info, @Local Optional<RegistryEntry<Potion>> optional) {
        RegistryEntry<Potion> potion = optional.get();
        for (BrewingRecipe<RegistryEntry<Potion>> recipe : this.potionRecipes) {
            if (recipe.input() == potion && ingredient.itematic$isOf(recipe.ingredient())) {
                info.setReturnValue(true);
                return;
            }
        }

        info.setReturnValue(false);
    }

    /**
     * @author ErrorCraft
     * @reason Uses item keys instead of direct items.
     */
    @Overwrite
    public boolean isBrewable(RegistryEntry<Potion> potion) {
        for (BrewingRecipe<RegistryEntry<Potion>> recipe : this.potionRecipes) {
            if (recipe.output() == potion) {
                return true;
            }
        }

        return false;
    }

    @Redirect(
        method = "create",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/BrewingRecipeRegistry;registerDefaults(Lnet/minecraft/recipe/BrewingRecipeRegistry$Builder;)V"
        )
    )
    private static void registerRecipes(BrewingRecipeRegistry.Builder builder) {
        builder.itematic$registerItemRecipe(ItemKeys.POTION, ItemKeys.GUNPOWDER, ItemKeys.SPLASH_POTION);
        builder.itematic$registerItemRecipe(ItemKeys.SPLASH_POTION, ItemKeys.DRAGON_BREATH, ItemKeys.LINGERING_POTION);

        builder.itematic$registerWaterPotionRecipe(ItemKeys.GLISTERING_MELON_SLICE, Potions.MUNDANE);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.GHAST_TEAR, Potions.MUNDANE);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.RABBIT_FOOT, Potions.MUNDANE);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.BLAZE_POWDER, Potions.MUNDANE);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.SPIDER_EYE, Potions.MUNDANE);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.SUGAR, Potions.MUNDANE);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.MAGMA_CREAM, Potions.MUNDANE);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.GLOWSTONE_DUST, Potions.THICK);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.REDSTONE, Potions.MUNDANE);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.NETHER_WART, Potions.AWKWARD);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.BREEZE_ROD, Potions.MUNDANE);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.SLIME_BLOCK, Potions.MUNDANE);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.STONE, Potions.MUNDANE);
        builder.itematic$registerWaterPotionRecipe(ItemKeys.COBWEB, Potions.MUNDANE);

        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.GOLDEN_CARROT, Potions.NIGHT_VISION);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.MAGMA_CREAM, Potions.FIRE_RESISTANCE);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.RABBIT_FOOT, Potions.LEAPING);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.TURTLE_HELMET, Potions.TURTLE_MASTER);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.SUGAR, Potions.SWIFTNESS);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.PUFFERFISH, Potions.WATER_BREATHING);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.GLISTERING_MELON_SLICE, Potions.HEALING);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.SPIDER_EYE, Potions.POISON);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.GHAST_TEAR, Potions.REGENERATION);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.BLAZE_POWDER, Potions.STRENGTH);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.PHANTOM_MEMBRANE, Potions.SLOW_FALLING);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.BREEZE_ROD, Potions.WIND_CHARGED);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.SLIME_BLOCK, Potions.OOZING);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.STONE, Potions.INFESTED);
        builder.itematic$registerAwkwardPotionRecipe(ItemKeys.COBWEB, Potions.WEAVING);

        builder.itematic$registerLongPotionRecipe(Potions.NIGHT_VISION, Potions.LONG_NIGHT_VISION);
        builder.itematic$registerLongPotionRecipe(Potions.INVISIBILITY, Potions.LONG_INVISIBILITY);
        builder.itematic$registerLongPotionRecipe(Potions.FIRE_RESISTANCE, Potions.LONG_FIRE_RESISTANCE);
        builder.itematic$registerLongPotionRecipe(Potions.LEAPING, Potions.LONG_LEAPING);
        builder.itematic$registerLongPotionRecipe(Potions.SLOWNESS, Potions.LONG_SLOWNESS);
        builder.itematic$registerLongPotionRecipe(Potions.TURTLE_MASTER, Potions.LONG_TURTLE_MASTER);
        builder.itematic$registerLongPotionRecipe(Potions.SWIFTNESS, Potions.LONG_SWIFTNESS);
        builder.itematic$registerLongPotionRecipe(Potions.WATER_BREATHING, Potions.LONG_WATER_BREATHING);
        builder.itematic$registerLongPotionRecipe(Potions.POISON, Potions.LONG_POISON);
        builder.itematic$registerLongPotionRecipe(Potions.REGENERATION, Potions.LONG_REGENERATION);
        builder.itematic$registerLongPotionRecipe(Potions.STRENGTH, Potions.LONG_STRENGTH);
        builder.itematic$registerLongPotionRecipe(Potions.WEAKNESS, Potions.LONG_WEAKNESS);
        builder.itematic$registerLongPotionRecipe(Potions.SLOW_FALLING, Potions.LONG_SLOW_FALLING);

        builder.itematic$registerStrongPotionRecipe(Potions.LEAPING, Potions.STRONG_LEAPING);
        builder.itematic$registerStrongPotionRecipe(Potions.SLOWNESS, Potions.STRONG_SLOWNESS);
        builder.itematic$registerStrongPotionRecipe(Potions.TURTLE_MASTER, Potions.STRONG_TURTLE_MASTER);
        builder.itematic$registerStrongPotionRecipe(Potions.SWIFTNESS, Potions.STRONG_SWIFTNESS);
        builder.itematic$registerStrongPotionRecipe(Potions.HEALING, Potions.STRONG_HEALING);
        builder.itematic$registerStrongPotionRecipe(Potions.HARMING, Potions.STRONG_HARMING);
        builder.itematic$registerStrongPotionRecipe(Potions.POISON, Potions.STRONG_POISON);
        builder.itematic$registerStrongPotionRecipe(Potions.REGENERATION, Potions.STRONG_REGENERATION);
        builder.itematic$registerStrongPotionRecipe(Potions.STRENGTH, Potions.STRONG_STRENGTH);

        builder.itematic$registerNegatingPotionRecipe(Potions.NIGHT_VISION, Potions.INVISIBILITY);
        builder.itematic$registerNegatingPotionRecipe(Potions.LONG_NIGHT_VISION, Potions.LONG_INVISIBILITY);
        builder.itematic$registerNegatingPotionRecipe(Potions.LEAPING, Potions.SLOWNESS);
        builder.itematic$registerNegatingPotionRecipe(Potions.LONG_LEAPING, Potions.LONG_SLOWNESS);
        builder.itematic$registerNegatingPotionRecipe(Potions.SWIFTNESS, Potions.SLOWNESS);
        builder.itematic$registerNegatingPotionRecipe(Potions.LONG_SWIFTNESS, Potions.LONG_SLOWNESS);
        builder.itematic$registerNegatingPotionRecipe(Potions.HEALING, Potions.HARMING);
        builder.itematic$registerNegatingPotionRecipe(Potions.STRONG_HEALING, Potions.STRONG_HARMING);
        builder.itematic$registerNegatingPotionRecipe(Potions.POISON, Potions.HARMING);
        builder.itematic$registerNegatingPotionRecipe(Potions.LONG_POISON, Potions.HARMING);
        builder.itematic$registerNegatingPotionRecipe(Potions.STRONG_POISON, Potions.STRONG_HARMING);
        builder.itematic$registerNegatingPotionRecipe(Potions.WATER, Potions.WEAKNESS);
    }

    @Override
    public void itematic$setItemRecipes(List<BrewingRecipe<RegistryKey<Item>>> itemRecipes) {
        this.itemRecipes = itemRecipes;
    }

    @Override
    public void itematic$setPotionRecipes(List<BrewingRecipe<RegistryEntry<Potion>>> potionRecipes) {
        this.potionRecipes = potionRecipes;
    }

    @Override
    public ItemStack itematic$craft(ItemStack ingredient, ItemStack input, World world) {
        if (input.isEmpty()) {
            return input;
        }

        Optional<RegistryEntry<Potion>> optionalPotion = input.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT)
            .potion();
        if (optionalPotion.isEmpty()) {
            return input;
        }

        RegistryEntry<Potion> potion = optionalPotion.get();
        for (BrewingRecipe<RegistryKey<Item>> recipe : this.itemRecipes) {
            if (input.itematic$isOf(recipe.input()) && ingredient.itematic$isOf(recipe.ingredient())) {
                return PotionContentsComponentUtil.setPotion(world.itematic$createStack(recipe.output()), potion);
            }
        }

        for (BrewingRecipe<RegistryEntry<Potion>> recipe : this.potionRecipes) {
            if (recipe.input() == potion && ingredient.itematic$isOf(recipe.ingredient())) {
                return PotionContentsComponentUtil.setPotion(new ItemStack(input.getRegistryEntry()), recipe.output());
            }
        }

        return input;
    }

    @Mixin(BrewingRecipeRegistry.Builder.class)
    public static class BuilderExtender implements BrewingRecipeRegistryBuilderAccess {
        @Unique
        private final List<BrewingRecipe<RegistryKey<Item>>> itemRecipes = new ArrayList<>();

        @Unique
        private final List<BrewingRecipe<RegistryEntry<Potion>>> potionRecipes = new ArrayList<>();

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        private BrewingRecipeRegistry setRecipes(BrewingRecipeRegistry original) {
            original.itematic$setItemRecipes(List.copyOf(this.itemRecipes));
            original.itematic$setPotionRecipes(List.copyOf(this.potionRecipes));
            return original;
        }

        @Override
        public void itematic$registerItemRecipe(RegistryKey<Item> from, RegistryKey<Item> ingredient, RegistryKey<Item> to) {
            this.itemRecipes.add(new BrewingRecipe<>(from, ingredient, to));
        }

        @Override
        public void itematic$registerWaterPotionRecipe(RegistryKey<Item> key, RegistryEntry<Potion> output) {
            this.potionRecipes.add(new BrewingRecipe<>(Potions.WATER, key, output));
        }

        @Override
        public void itematic$registerAwkwardPotionRecipe(RegistryKey<Item> key, RegistryEntry<Potion> output) {
            this.potionRecipes.add(new BrewingRecipe<>(Potions.AWKWARD, key, output));
        }

        @Override
        public void itematic$registerLongPotionRecipe(RegistryEntry<Potion> input, RegistryEntry<Potion> output) {
            this.potionRecipes.add(new BrewingRecipe<>(input, ItemKeys.REDSTONE, output));
        }

        @Override
        public void itematic$registerStrongPotionRecipe(RegistryEntry<Potion> input, RegistryEntry<Potion> output) {
            this.potionRecipes.add(new BrewingRecipe<>(input, ItemKeys.GLOWSTONE_DUST, output));
        }

        @Override
        public void itematic$registerNegatingPotionRecipe(RegistryEntry<Potion> input, RegistryEntry<Potion> output) {
            this.potionRecipes.add(new BrewingRecipe<>(input, ItemKeys.FERMENTED_SPIDER_EYE, output));
        }
    }
}
