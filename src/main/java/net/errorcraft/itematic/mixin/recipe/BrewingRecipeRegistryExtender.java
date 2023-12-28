package net.errorcraft.itematic.mixin.recipe;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.recipe.BrewingRecipeRegistryAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.recipe.BrewingRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
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
import java.util.function.Predicate;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryExtender implements BrewingRecipeRegistryAccess {
    @Unique
    private static final List<BrewingRecipe<RegistryKey<Item>>> ITEM_RECIPES = List.of(
        new BrewingRecipe<>(ItemKeys.POTION, ItemKeys.GUNPOWDER, ItemKeys.SPLASH_POTION),
        new BrewingRecipe<>(ItemKeys.SPLASH_POTION, ItemKeys.DRAGON_BREATH, ItemKeys.LINGERING_POTION)
    );
    @Unique
    private static final List<BrewingRecipe<RegistryEntry<Potion>>> POTION_RECIPES = new ArrayList<>();

    @Redirect(
        method = "hasRecipe",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"
        )
    )
    private static <T> boolean hasRecipeTestUseItemComponentCheck(Predicate<Item> instance, T t) {
        return ((ItemStack) t).itematic$hasComponent(ItemComponentTypes.POTION_HOLDER);
    }

    /**
     * @author ErrorCraft
     * @reason Uses item keys instead of direct items.
     */
    @Overwrite
    public static boolean isItemRecipeIngredient(ItemStack stack) {
        for (BrewingRecipe<RegistryKey<Item>> recipe : ITEM_RECIPES) {
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
    public static boolean hasItemRecipe(ItemStack input, ItemStack ingredient) {
        for (BrewingRecipe<RegistryKey<Item>> recipe : ITEM_RECIPES) {
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
    public static boolean isPotionRecipeIngredient(ItemStack stack) {
        for (BrewingRecipe<RegistryEntry<Potion>> recipe : POTION_RECIPES) {
            if (stack.itematic$isOf(recipe.ingredient())) {
                return true;
            }
        }
        return false;
    }

    @Inject(
        method = "hasPotionRecipe",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/potion/PotionUtil;getPotion(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/registry/entry/RegistryEntry;"
        ),
        cancellable = true
    )
    private static void hasPotionRecipeUseItemKeys(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> info, @Local RegistryEntry<Potion> potion) {
        for (BrewingRecipe<RegistryEntry<Potion>> recipe : POTION_RECIPES) {
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
    public static boolean isBrewable(RegistryEntry<Potion> potion) {
        for (BrewingRecipe<RegistryEntry<Potion>> recipe : POTION_RECIPES) {
            if (recipe.output() == potion) {
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
    public static void registerDefaults() {
        registerWaterPotionRecipe(ItemKeys.GLISTERING_MELON_SLICE, Potions.MUNDANE);
        registerWaterPotionRecipe(ItemKeys.GHAST_TEAR, Potions.MUNDANE);
        registerWaterPotionRecipe(ItemKeys.RABBIT_FOOT, Potions.MUNDANE);
        registerWaterPotionRecipe(ItemKeys.BLAZE_POWDER, Potions.MUNDANE);
        registerWaterPotionRecipe(ItemKeys.SPIDER_EYE, Potions.MUNDANE);
        registerWaterPotionRecipe(ItemKeys.SUGAR, Potions.MUNDANE);
        registerWaterPotionRecipe(ItemKeys.MAGMA_CREAM, Potions.MUNDANE);
        registerWaterPotionRecipe(ItemKeys.GLOWSTONE_DUST, Potions.THICK);
        registerWaterPotionRecipe(ItemKeys.REDSTONE, Potions.MUNDANE);
        registerWaterPotionRecipe(ItemKeys.NETHER_WART, Potions.AWKWARD);

        registerAwkwardPotionRecipe(ItemKeys.GOLDEN_CARROT, Potions.NIGHT_VISION);
        registerAwkwardPotionRecipe(ItemKeys.MAGMA_CREAM, Potions.FIRE_RESISTANCE);
        registerAwkwardPotionRecipe(ItemKeys.RABBIT_FOOT, Potions.LEAPING);
        registerAwkwardPotionRecipe(ItemKeys.TURTLE_HELMET, Potions.TURTLE_MASTER);
        registerAwkwardPotionRecipe(ItemKeys.SUGAR, Potions.SWIFTNESS);
        registerAwkwardPotionRecipe(ItemKeys.PUFFERFISH, Potions.WATER_BREATHING);
        registerAwkwardPotionRecipe(ItemKeys.GLISTERING_MELON_SLICE, Potions.HEALING);
        registerAwkwardPotionRecipe(ItemKeys.SPIDER_EYE, Potions.POISON);
        registerAwkwardPotionRecipe(ItemKeys.GHAST_TEAR, Potions.REGENERATION);
        registerAwkwardPotionRecipe(ItemKeys.BLAZE_POWDER, Potions.STRENGTH);
        registerAwkwardPotionRecipe(ItemKeys.PHANTOM_MEMBRANE, Potions.SLOW_FALLING);

        registerLongPotionRecipe(Potions.NIGHT_VISION, Potions.LONG_NIGHT_VISION);
        registerLongPotionRecipe(Potions.INVISIBILITY, Potions.LONG_INVISIBILITY);
        registerLongPotionRecipe(Potions.FIRE_RESISTANCE, Potions.LONG_FIRE_RESISTANCE);
        registerLongPotionRecipe(Potions.LEAPING, Potions.LONG_LEAPING);
        registerLongPotionRecipe(Potions.SLOWNESS, Potions.LONG_SLOWNESS);
        registerLongPotionRecipe(Potions.TURTLE_MASTER, Potions.LONG_TURTLE_MASTER);
        registerLongPotionRecipe(Potions.SWIFTNESS, Potions.LONG_SWIFTNESS);
        registerLongPotionRecipe(Potions.WATER_BREATHING, Potions.LONG_WATER_BREATHING);
        registerLongPotionRecipe(Potions.POISON, Potions.LONG_POISON);
        registerLongPotionRecipe(Potions.REGENERATION, Potions.LONG_REGENERATION);
        registerLongPotionRecipe(Potions.STRENGTH, Potions.LONG_STRENGTH);
        registerLongPotionRecipe(Potions.WEAKNESS, Potions.LONG_WEAKNESS);
        registerLongPotionRecipe(Potions.SLOW_FALLING, Potions.LONG_SLOW_FALLING);

        registerStrongPotionRecipe(Potions.LEAPING, Potions.STRONG_LEAPING);
        registerStrongPotionRecipe(Potions.SLOWNESS, Potions.STRONG_SLOWNESS);
        registerStrongPotionRecipe(Potions.TURTLE_MASTER, Potions.STRONG_TURTLE_MASTER);
        registerStrongPotionRecipe(Potions.SWIFTNESS, Potions.STRONG_SWIFTNESS);
        registerStrongPotionRecipe(Potions.HEALING, Potions.STRONG_HEALING);
        registerStrongPotionRecipe(Potions.HARMING, Potions.STRONG_HARMING);
        registerStrongPotionRecipe(Potions.POISON, Potions.STRONG_POISON);
        registerStrongPotionRecipe(Potions.REGENERATION, Potions.STRONG_REGENERATION);
        registerStrongPotionRecipe(Potions.STRENGTH, Potions.STRONG_STRENGTH);

        registerNegatingPotionRecipe(Potions.NIGHT_VISION, Potions.INVISIBILITY);
        registerNegatingPotionRecipe(Potions.LONG_NIGHT_VISION, Potions.LONG_INVISIBILITY);
        registerNegatingPotionRecipe(Potions.LEAPING, Potions.SLOWNESS);
        registerNegatingPotionRecipe(Potions.LONG_LEAPING, Potions.LONG_SLOWNESS);
        registerNegatingPotionRecipe(Potions.SWIFTNESS, Potions.SLOWNESS);
        registerNegatingPotionRecipe(Potions.LONG_SWIFTNESS, Potions.LONG_SLOWNESS);
        registerNegatingPotionRecipe(Potions.HEALING, Potions.HARMING);
        registerNegatingPotionRecipe(Potions.STRONG_HEALING, Potions.STRONG_HARMING);
        registerNegatingPotionRecipe(Potions.POISON, Potions.HARMING);
        registerNegatingPotionRecipe(Potions.LONG_POISON, Potions.HARMING);
        registerNegatingPotionRecipe(Potions.STRONG_POISON, Potions.STRONG_HARMING);
        registerNegatingPotionRecipe(Potions.WATER, Potions.WEAKNESS);
    }

    @Override
    public ItemStack itematic$craft(ItemStack ingredient, ItemStack input, World world) {
        if (input.isEmpty()) {
            return input;
        }
        RegistryEntry<Potion> potion = PotionUtil.getPotion(input);
        for (BrewingRecipe<RegistryKey<Item>> recipe : ITEM_RECIPES) {
            if (input.itematic$isOf(recipe.input()) && ingredient.itematic$isOf(recipe.ingredient())) {
                return PotionUtil.setPotion(world.itematic$createStack(recipe.output()), potion);
            }
        }
        for (BrewingRecipe<RegistryEntry<Potion>> recipe : POTION_RECIPES) {
            if (recipe.input() == potion && ingredient.itematic$isOf(recipe.ingredient())) {
                return PotionUtil.setPotion(new ItemStack(input.getRegistryEntry()), recipe.output());
            }
        }
        return input;
    }

    @Unique
    private static void registerWaterPotionRecipe(RegistryKey<Item> key, RegistryEntry<Potion> output) {
        POTION_RECIPES.add(new BrewingRecipe<>(Potions.WATER, key, output));
    }

    @Unique
    private static void registerAwkwardPotionRecipe(RegistryKey<Item> key, RegistryEntry<Potion> output) {
        POTION_RECIPES.add(new BrewingRecipe<>(Potions.AWKWARD, key, output));
    }

    @Unique
    private static void registerLongPotionRecipe(RegistryEntry<Potion> input, RegistryEntry<Potion> output) {
        POTION_RECIPES.add(new BrewingRecipe<>(input, ItemKeys.REDSTONE, output));
    }

    @Unique
    private static void registerStrongPotionRecipe(RegistryEntry<Potion> input, RegistryEntry<Potion> output) {
        POTION_RECIPES.add(new BrewingRecipe<>(input, ItemKeys.GLOWSTONE_DUST, output));
    }

    @Unique
    private static void registerNegatingPotionRecipe(RegistryEntry<Potion> input, RegistryEntry<Potion> output) {
        POTION_RECIPES.add(new BrewingRecipe<>(input, ItemKeys.FERMENTED_SPIDER_EYE, output));
    }
}
