package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import com.google.common.collect.ImmutableList;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

public class RecipeAlternativesWidgetExtender {
    @Mixin(targets = "net/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayCraftingRecipeButton")
    public static abstract class CraftingAlternativeButtonWidgetExtender extends AbstractWidget {
        @Unique
        private static final Identifier BREWING_TEXTURE_ENABLED = Identifier.withDefaultNamespace("recipe_book/brewing_stand_overlay");
        @Unique
        private static final Identifier BREWING_TEXTURE_ENABLED_HIGHLIGHTED = Identifier.withDefaultNamespace("recipe_book/brewing_stand_overlay_highlighted");
        @Unique
        private static final Identifier BREWING_TEXTURE_DISABLED = Identifier.withDefaultNamespace("recipe_book/brewing_stand_overlay_disabled");
        @Unique
        private static final Identifier BREWING_TEXTURE_DISABLED_HIGHLIGHTED = Identifier.withDefaultNamespace("recipe_book/brewing_stand_overlay_disabled_highlighted");

        @Unique
        private boolean isBrewingRecipe;

        public CraftingAlternativeButtonWidgetExtender(int x, int y, int width, int height, Component message) {
            super(x, y, width, height, message);
        }

        @Inject(
            method = "<init>",
            at = @At("TAIL")
        )
        private void setBrewingRecipe(OverlayRecipeComponent recipeAlternativesWidget, int x, int y, RecipeDisplayId recipeId, RecipeDisplay display, ContextMap context, boolean craftable, CallbackInfo info) {
            this.isBrewingRecipe = display instanceof BrewingRecipeDisplay;
        }

        @Inject(
            method = "getSprite",
            at = @At("HEAD"),
            cancellable = true
        )
        protected void getOverlayTexture(boolean enabled, CallbackInfoReturnable<Identifier> info) {
            if (!this.isBrewingRecipe) {
                return;
            }

            info.setReturnValue(this.getBrewingStandOverlayTexture(enabled));
        }

        @Inject(
            method = "calculateIngredientsPositions",
            at = @At("HEAD"),
            cancellable = true
        )
        private static void checkBrewingRecipe(RecipeDisplay display, ContextMap context, CallbackInfoReturnable<List<?>> info) {
            if (!(display instanceof BrewingRecipeDisplay brewingRecipeDisplay)) {
                return;
            }

            ImmutableList.Builder<Object> slots = new ImmutableList.Builder<>();
            List<ItemStack> bases = brewingRecipeDisplay.base().resolveForStacks(context);
            if (!bases.isEmpty()) {
                slots.add(RecipeAlternativesWidgetAccessor.AlternativeButtonWidgetAccessor.slot(0, 2, bases));
            }

            List<ItemStack> reagents = brewingRecipeDisplay.reagent().resolveForStacks(context);
            if (!reagents.isEmpty()) {
                slots.add(RecipeAlternativesWidgetAccessor.AlternativeButtonWidgetAccessor.slot(1, 0, reagents));
            }

            info.setReturnValue(slots.build());
        }

        @Unique
        private Identifier getBrewingStandOverlayTexture(boolean enabled) {
            if (enabled) {
                return this.isHoveredOrFocused() ? BREWING_TEXTURE_ENABLED_HIGHLIGHTED : BREWING_TEXTURE_ENABLED;
            }

            return this.isHoveredOrFocused() ? BREWING_TEXTURE_DISABLED_HIGHLIGHTED : BREWING_TEXTURE_DISABLED;
        }
    }
}
