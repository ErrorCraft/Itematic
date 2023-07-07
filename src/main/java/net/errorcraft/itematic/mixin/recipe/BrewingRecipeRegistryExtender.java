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
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

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
    private static final List<BrewingRecipe<Potion>> POTION_RECIPES = new ArrayList<>();

    @Redirect(
        method = "hasRecipe",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"
        )
    )
    private static <T> boolean hasRecipeTestUseItemComponentCheck(Predicate<Item> instance, T t) {
        return ((ItemStack) t).hasComponent(ItemComponentTypes.POTION_HOLDER);
    }

    @Redirect(
        method = { "isItemRecipeIngredient", "hasItemRecipe" },
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;size()I",
            ordinal = 0
        )
    )
    private static int itemRecipesSizeUseRegistryKeyList(List<?> instance) {
        return ITEM_RECIPES.size();
    }

    @Redirect(
        method = { "isPotionRecipeIngredient", "isBrewable", "hasPotionRecipe" },
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;size()I",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/recipe/BrewingRecipeRegistry;POTION_RECIPES:Ljava/util/List;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static int potionRecipesSizeUseRegistryKeyList(List<?> instance) {
        return POTION_RECIPES.size();
    }

    @Redirect(
        method = { "isItemRecipeIngredient", "isPotionRecipeIngredient", "isBrewable", "hasItemRecipe", "hasPotionRecipe" },
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;get(I)Ljava/lang/Object;"
        )
    )
    private static <E> E recipesGetReturnNull(List<E> instance, int i) {
        return null;
    }

    @Redirect(
        method = { "isItemRecipeIngredient", "isPotionRecipeIngredient", "hasItemRecipe", "hasPotionRecipe" },
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/recipe/BrewingRecipeRegistry$Recipe;ingredient:Lnet/minecraft/recipe/Ingredient;",
            opcode = Opcodes.GETFIELD
        )
    )
    private static Ingredient recipesGetIngredientReturnNull(@Coerce Object instance) {
        return null;
    }

    @Redirect(
        method = "hasItemRecipe",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/recipe/BrewingRecipeRegistry$Recipe;input:Ljava/lang/Object;",
            opcode = Opcodes.GETFIELD
        )
    )
    private static Object hasItemRecipeGetInputForceItemForComparison(@Coerce Object instance, @Local Item item) {
        return item;
    }

    @Redirect(
        method = "hasPotionRecipe",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/recipe/BrewingRecipeRegistry$Recipe;input:Ljava/lang/Object;",
            opcode = Opcodes.GETFIELD
        )
    )
    private static Object hasPotionRecipeGetInputForcePotionForComparison(@Coerce Object instance, @Local Potion potion) {
        return potion;
    }

    @Redirect(
        method = "isItemRecipeIngredient",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0
        )
    )
    private static boolean isItemRecipeIngredientTestUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack, @Local(ordinal = 0) int i) {
        return itemStack.isOf(ITEM_RECIPES.get(i).ingredient());
    }

    @Redirect(
        method = "hasItemRecipe",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0
        )
    )
    private static boolean hasItemRecipeTestUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack, @Local(ordinal = 0) ItemStack input, @Local(ordinal = 0) int i) {
        BrewingRecipe<RegistryKey<Item>> recipe = ITEM_RECIPES.get(i);
        return input.isOf(recipe.input()) && itemStack.isOf(recipe.ingredient());
    }

    @Redirect(
        method = "isPotionRecipeIngredient",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private static boolean isPotionRecipeIngredientTestUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack, @Local(ordinal = 0) int i) {
        return itemStack.isOf(POTION_RECIPES.get(i).ingredient());
    }

    @Redirect(
        method = "hasPotionRecipe",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private static boolean hasPotionRecipeTestUseRegistryKeyCheck(Ingredient instance, ItemStack itemStack, @Local Potion potion, @Local(ordinal = 0) int i) {
        BrewingRecipe<Potion> recipe = POTION_RECIPES.get(i);
        return recipe.input() == potion && itemStack.isOf(recipe.ingredient());
    }

    @Redirect(
        method = "isBrewable",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/recipe/BrewingRecipeRegistry$Recipe;output:Ljava/lang/Object;",
            opcode = Opcodes.GETFIELD
        )
    )
    private static Object isBrewableGetOutputUseRegistryKeyList(@Coerce Object instance, @Local(ordinal = 0) int i) {
        return POTION_RECIPES.get(i).output();
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
    public ItemStack craft(ItemStack ingredient, ItemStack input, World world) {
        if (input.isEmpty()) {
            return input;
        }
        Potion potion = PotionUtil.getPotion(input);
        for (BrewingRecipe<RegistryKey<Item>> recipe : ITEM_RECIPES) {
            if (input.isOf(recipe.input()) && ingredient.isOf(recipe.ingredient())) {
                return PotionUtil.setPotion(new ItemStack(world.getItem(recipe.output())), potion);
            }
        }
        for (BrewingRecipe<Potion> recipe : POTION_RECIPES) {
            if (recipe.input() == potion && ingredient.isOf(recipe.ingredient())) {
                return PotionUtil.setPotion(new ItemStack(input.getRegistryEntry()), recipe.output());
            }
        }
        return input;
    }

    private static void registerWaterPotionRecipe(RegistryKey<Item> key, Potion output) {
        POTION_RECIPES.add(new BrewingRecipe<>(Potions.WATER, key, output));
    }

    private static void registerAwkwardPotionRecipe(RegistryKey<Item> key, Potion output) {
        POTION_RECIPES.add(new BrewingRecipe<>(Potions.AWKWARD, key, output));
    }

    private static void registerLongPotionRecipe(Potion input, Potion output) {
        POTION_RECIPES.add(new BrewingRecipe<>(input, ItemKeys.REDSTONE, output));
    }

    private static void registerStrongPotionRecipe(Potion input, Potion output) {
        POTION_RECIPES.add(new BrewingRecipe<>(input, ItemKeys.GLOWSTONE_DUST, output));
    }

    private static void registerNegatingPotionRecipe(Potion input, Potion output) {
        POTION_RECIPES.add(new BrewingRecipe<>(input, ItemKeys.FERMENTED_SPIDER_EYE, output));
    }
}
