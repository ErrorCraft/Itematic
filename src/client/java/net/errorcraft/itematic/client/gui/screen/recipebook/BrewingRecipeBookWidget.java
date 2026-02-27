package net.errorcraft.itematic.client.gui.screen.recipebook;

import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.errorcraft.itematic.screen.BrewingStandMenuDelegate;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class BrewingRecipeBookWidget extends RecipeBookWidget {
    private static final ButtonTextures TEXTURES = new ButtonTextures(
        Identifier.ofVanilla("recipe_book/brewing_stand_filter_enabled"),
        Identifier.ofVanilla("recipe_book/brewing_stand_filter_disabled"),
        Identifier.ofVanilla("recipe_book/brewing_stand_filter_enabled_highlighted"),
        Identifier.ofVanilla("recipe_book/brewing_stand_filter_disabled_highlighted")
    );
    private static final Text TOGGLE_BREWABLE_TEXT = Text.translatable("gui.recipebook.toggleRecipes.brewable");

    @Override
    protected void setBookButtonTexture() {
        this.toggleCraftableButton.setTextures(TEXTURES);
    }

    @Override
    protected Text getToggleCraftableButtonText() {
        return TOGGLE_BREWABLE_TEXT;
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void showGhostRecipe(RecipeEntry<?> recipe, List<Slot> slots) {
        this.ghostSlots.setRecipe(recipe);
        DynamicRegistryManager registryManager = this.client.world.getRegistryManager();
        RegistryWrapper.Impl<Item> items = registryManager.getWrapperOrThrow(RegistryKeys.ITEM);
        if (recipe.value() instanceof BrewingRecipe<?> brewingRecipe) {
            Slot firstInputSlot = this.craftingScreenHandler.getSlot(BrewingStandMenuDelegate.FIRST_INPUT_SLOT);
            this.ghostSlots.addSlot(brewingRecipe.inputIngredient(items), firstInputSlot.x, firstInputSlot.y);

            Slot ingredientSlot = this.craftingScreenHandler.getSlot(BrewingStandMenuDelegate.INGREDIENT_SLOT);
            this.ghostSlots.addSlot(brewingRecipe.additionIngredient(), ingredientSlot.x, ingredientSlot.y);
        }
    }
}
