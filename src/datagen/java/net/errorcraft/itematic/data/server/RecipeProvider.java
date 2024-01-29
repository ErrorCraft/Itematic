package net.errorcraft.itematic.data.server;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.recipe.ItemColoringRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class RecipeProvider extends FabricCodecDataProvider<Recipe<?>> {
    public RecipeProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK, "recipes", Recipe.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Recipe<?>> provider, RegistryWrapper.WrapperLookup lookup) {
        RegistryWrapper.Impl<Item> items = lookup.getWrapperOrThrow(RegistryKeys.ITEM);
        provider.accept(new Identifier("shulker_box_coloring_black"), colorShulkerBox(
            DyeColor.BLACK,
            items.getOrThrow(ItemKeys.BLACK_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_blue"), colorShulkerBox(
            DyeColor.BLUE,
            items.getOrThrow(ItemKeys.BLUE_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_brown"), colorShulkerBox(
            DyeColor.BROWN,
            items.getOrThrow(ItemKeys.BROWN_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_cyan"), colorShulkerBox(
            DyeColor.CYAN,
            items.getOrThrow(ItemKeys.CYAN_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_gray"), colorShulkerBox(
            DyeColor.GRAY,
            items.getOrThrow(ItemKeys.GRAY_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_green"), colorShulkerBox(
            DyeColor.GREEN,
            items.getOrThrow(ItemKeys.GREEN_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_light_blue"), colorShulkerBox(
            DyeColor.LIGHT_BLUE,
            items.getOrThrow(ItemKeys.LIGHT_BLUE_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_light_gray"), colorShulkerBox(
            DyeColor.LIGHT_GRAY,
            items.getOrThrow(ItemKeys.LIGHT_GRAY_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_lime"), colorShulkerBox(
            DyeColor.LIME,
            items.getOrThrow(ItemKeys.LIME_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_magenta"), colorShulkerBox(
            DyeColor.MAGENTA,
            items.getOrThrow(ItemKeys.MAGENTA_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_orange"), colorShulkerBox(
            DyeColor.ORANGE,
            items.getOrThrow(ItemKeys.ORANGE_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_pink"), colorShulkerBox(
            DyeColor.PINK,
            items.getOrThrow(ItemKeys.PINK_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_purple"), colorShulkerBox(
            DyeColor.PURPLE,
            items.getOrThrow(ItemKeys.PURPLE_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_red"), colorShulkerBox(
            DyeColor.RED,
            items.getOrThrow(ItemKeys.RED_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_white"), colorShulkerBox(
            DyeColor.WHITE,
            items.getOrThrow(ItemKeys.WHITE_SHULKER_BOX)
        ));
        provider.accept(new Identifier("shulker_box_coloring_yellow"), colorShulkerBox(
            DyeColor.YELLOW,
            items.getOrThrow(ItemKeys.YELLOW_SHULKER_BOX)
        ));
    }

    @Override
    public String getName() {
        return "Recipes";
    }

    private static ItemColoringRecipe colorShulkerBox(DyeColor color, RegistryEntry<Item> result) {
        return new ItemColoringRecipe(CraftingRecipeCategory.MISC, Ingredient.fromTag(ItematicItemTags.SHULKER_BOXES), color, new ItemStack(result));
    }
}
