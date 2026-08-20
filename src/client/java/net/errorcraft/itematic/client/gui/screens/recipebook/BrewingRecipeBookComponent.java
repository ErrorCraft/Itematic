package net.errorcraft.itematic.client.gui.screens.recipebook;

import net.errorcraft.itematic.mixin.client.gui.screens.recipebook.GhostSlotsAccessor;
import net.errorcraft.itematic.world.inventory.BrewingStandMenuDelegate;
import net.errorcraft.itematic.world.item.crafting.ItematicRecipeBookCategories;
import net.errorcraft.itematic.world.item.crafting.display.BrewingRecipeDisplay;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;

public class BrewingRecipeBookComponent extends RecipeBookComponent<BrewingStandMenuDelegate> {
    private static final WidgetSprites TEXTURES = new WidgetSprites(
        Identifier.withDefaultNamespace("recipe_book/brewing_stand_filter_enabled"),
        Identifier.withDefaultNamespace("recipe_book/brewing_stand_filter_disabled"),
        Identifier.withDefaultNamespace("recipe_book/brewing_stand_filter_enabled_highlighted"),
        Identifier.withDefaultNamespace("recipe_book/brewing_stand_filter_disabled_highlighted")
    );
    private static final Component TOGGLE_BREWABLE_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.brewable");
    private static final List<TabInfo> TABS = List.of(
        new TabInfo(SearchRecipeBookCategory.ITEMATIC_BREWING),
        // Item references are intended as key conversion is handled by a mixin
        new TabInfo(Items.NETHER_WART, Items.MAGMA_CREAM, ItematicRecipeBookCategories.BREWING_MODIFY),
        new TabInfo(Items.SPLASH_POTION, Items.LINGERING_POTION, ItematicRecipeBookCategories.BREWING_AMPLIFY)
    );

    public BrewingRecipeBookComponent(BrewingStandMenuDelegate menu) {
        super(menu, TABS);
    }

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return TEXTURES;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return false;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents stackedContents) {
        collection.selectRecipes(
            stackedContents,
            display -> display instanceof BrewingRecipeDisplay
        );
    }

    @Override
    protected Component getRecipeFilterName() {
        return TOGGLE_BREWABLE_TOOLTIP;
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipe, ContextMap context) {
        if (recipe instanceof BrewingRecipeDisplay brewingRecipe) {
            ((GhostSlotsAccessor) ghostSlots).itematic$setInput(this.menu.firstInputSlot(), context, brewingRecipe.base());
            ((GhostSlotsAccessor) ghostSlots).itematic$setInput(this.menu.ingredientSlot(), context, brewingRecipe.reagent());
        }
    }
}
