package net.errorcraft.itematic.mixin.client.gui.screens.recipebook;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.errorcraft.itematic.world.item.crafting.display.BrewingRecipeDisplay;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

public class OverlayRecipeComponentExtender {
    @Mixin(targets = "net/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayCraftingRecipeButton")
    public static abstract class OverlayCraftingRecipeButtonExtender extends AbstractWidget {
        @Unique
        private static final Identifier BREWING_ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/brewing_stand_overlay");
        @Unique
        private static final Identifier BREWING_HIGHLIGHTED_ENABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/brewing_stand_overlay_highlighted");
        @Unique
        private static final Identifier BREWING_DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/brewing_stand_overlay_disabled");
        @Unique
        private static final Identifier BREWING_HIGHLIGHTED_DISABLED_SPRITE = Identifier.withDefaultNamespace("recipe_book/brewing_stand_overlay_disabled_highlighted");

        @Unique
        private boolean isBrewingRecipe;

        public OverlayCraftingRecipeButtonExtender(int x, int y, int width, int height, Component message) {
            super(x, y, width, height, message);
        }

        @Inject(
            method = "<init>",
            at = @At("TAIL")
        )
        private void setBrewingRecipe(OverlayRecipeComponent recipeAlternativesWidget, int x, int y, RecipeDisplayId recipeId, RecipeDisplay display, ContextMap context, boolean craftable, CallbackInfo info) {
            this.isBrewingRecipe = display instanceof BrewingRecipeDisplay;
        }

        @WrapMethod(
            method = "getSprite"
        )
        protected Identifier getOverlayTexture(boolean isCraftable, Operation<Identifier> original) {
            if (!this.isBrewingRecipe) {
                return this.getBrewingStandOverlayTexture(isCraftable);
            }

            return original.call(isCraftable);
        }

        @WrapMethod(
            method = "calculateIngredientsPositions"
        )
        private static List<OverlayRecipeComponent.OverlayRecipeButton.Pos> checkBrewingRecipe(RecipeDisplay recipe, ContextMap context, Operation<List<OverlayRecipeComponent.OverlayRecipeButton.Pos>> original) {
            if (!(recipe instanceof BrewingRecipeDisplay brewingRecipe)) {
                return original.call(recipe, context);
            }

            ImmutableList.Builder<OverlayRecipeComponent.OverlayRecipeButton.Pos> result = new ImmutableList.Builder<>();
            List<ItemStack> bases = brewingRecipe.base().resolveForStacks(context);
            if (!bases.isEmpty()) {
                result.add(OverlayRecipeComponentAccessor.OverlayRecipeButtonAccessor.createGridPos(0, 2, bases));
            }

            List<ItemStack> reagents = brewingRecipe.reagent().resolveForStacks(context);
            if (!reagents.isEmpty()) {
                result.add(OverlayRecipeComponentAccessor.OverlayRecipeButtonAccessor.createGridPos(1, 0, reagents));
            }

            return result.build();
        }

        @Unique
        private Identifier getBrewingStandOverlayTexture(boolean enabled) {
            if (enabled) {
                return this.isHoveredOrFocused()
                    ? BREWING_HIGHLIGHTED_ENABLED_SPRITE
                    : BREWING_ENABLED_SPRITE;
            }

            return this.isHoveredOrFocused()
                ? BREWING_HIGHLIGHTED_DISABLED_SPRITE
                : BREWING_DISABLED_SPRITE;
        }
    }
}
