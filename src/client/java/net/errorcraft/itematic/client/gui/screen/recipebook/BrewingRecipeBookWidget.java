package net.errorcraft.itematic.client.gui.screen.recipebook;

import net.errorcraft.itematic.mixin.client.gui.screen.recipebook.GhostRecipeAccessor;
import net.errorcraft.itematic.recipe.book.ItematicRecipeBookCategories;
import net.errorcraft.itematic.recipe.display.BrewingRecipeDisplay;
import net.errorcraft.itematic.screen.BrewingStandMenuDelegate;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.GhostRecipe;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.recipebook.RecipeBookType;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;

import java.util.List;

public class BrewingRecipeBookWidget extends RecipeBookWidget<BrewingStandMenuDelegate> {
    private static final ButtonTextures TEXTURES = new ButtonTextures(
        Identifier.ofVanilla("recipe_book/brewing_stand_filter_enabled"),
        Identifier.ofVanilla("recipe_book/brewing_stand_filter_disabled"),
        Identifier.ofVanilla("recipe_book/brewing_stand_filter_enabled_highlighted"),
        Identifier.ofVanilla("recipe_book/brewing_stand_filter_disabled_highlighted")
    );
    private static final Text TOGGLE_BREWABLE_TEXT = Text.translatable("gui.recipebook.toggleRecipes.brewable");
    private static final List<Tab> TABS = List.of(
        new Tab(RecipeBookType.ITEMATIC_BREWING),
        // Item references are intended as key conversion is handled by a mixin
        new Tab(Items.NETHER_WART, Items.MAGMA_CREAM, ItematicRecipeBookCategories.BREWING_MODIFY),
        new Tab(Items.SPLASH_POTION, Items.LINGERING_POTION, ItematicRecipeBookCategories.BREWING_AMPLIFY)
    );

    public BrewingRecipeBookWidget(BrewingStandMenuDelegate menu) {
        super(menu, TABS);
    }

    @Override
    protected void setBookButtonTexture() {
        this.toggleCraftableButton.setTextures(TEXTURES);
    }

    @Override
    protected boolean isValid(Slot slot) {
        return false;
    }

    @Override
    protected void populateRecipes(RecipeResultCollection recipeResultCollection, RecipeFinder recipeFinder) {
        recipeResultCollection.populateRecipes(recipeFinder, display -> display instanceof BrewingRecipeDisplay);
    }

    @Override
    protected Text getToggleCraftableButtonText() {
        return TOGGLE_BREWABLE_TEXT;
    }

    @Override
    protected void showGhostRecipe(GhostRecipe ghostRecipe, RecipeDisplay display, ContextParameterMap context) {
        if (display instanceof BrewingRecipeDisplay brewingRecipeDisplay) {
            ((GhostRecipeAccessor) ghostRecipe).itematic$addInputs(this.craftingScreenHandler.firstInputSlot(), context, brewingRecipeDisplay.base());
            ((GhostRecipeAccessor) ghostRecipe).itematic$addInputs(this.craftingScreenHandler.ingredientSlot(), context, brewingRecipeDisplay.reagent());
        }
    }
}
