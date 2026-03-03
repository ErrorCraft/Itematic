package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import com.google.common.collect.ImmutableList;
import net.errorcraft.itematic.recipe.display.BrewingRecipeDisplay;
import net.minecraft.client.gui.screen.recipebook.RecipeAlternativesWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

public class RecipeAlternativesWidgetExtender {
    @Mixin(targets = "net/minecraft/client/gui/screen/recipebook/RecipeAlternativesWidget$CraftingAlternativeButtonWidget")
    public static abstract class CraftingAlternativeButtonWidgetExtender extends ClickableWidget {
        @Unique
        private static final Identifier BREWING_TEXTURE_ENABLED = Identifier.ofVanilla("recipe_book/brewing_stand_overlay");
        @Unique
        private static final Identifier BREWING_TEXTURE_ENABLED_HIGHLIGHTED = Identifier.ofVanilla("recipe_book/brewing_stand_overlay_highlighted");
        @Unique
        private static final Identifier BREWING_TEXTURE_DISABLED = Identifier.ofVanilla("recipe_book/brewing_stand_overlay_disabled");
        @Unique
        private static final Identifier BREWING_TEXTURE_DISABLED_HIGHLIGHTED = Identifier.ofVanilla("recipe_book/brewing_stand_overlay_disabled_highlighted");

        @Unique
        private boolean isBrewingRecipe;

        public CraftingAlternativeButtonWidgetExtender(int x, int y, int width, int height, Text message) {
            super(x, y, width, height, message);
        }

        @Inject(
            method = "<init>",
            at = @At("TAIL")
        )
        private void setBrewingRecipe(RecipeAlternativesWidget recipeAlternativesWidget, int x, int y, NetworkRecipeId recipeId, RecipeDisplay display, ContextParameterMap context, boolean craftable, CallbackInfo info) {
            this.isBrewingRecipe = display instanceof BrewingRecipeDisplay;
        }

        @Inject(
            method = "getOverlayTexture",
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
            method = "collectInputSlots",
            at = @At("HEAD"),
            cancellable = true
        )
        private static void checkBrewingRecipe(RecipeDisplay display, ContextParameterMap context, CallbackInfoReturnable<List<?>> info) {
            if (!(display instanceof BrewingRecipeDisplay brewingRecipeDisplay)) {
                return;
            }

            ImmutableList.Builder<Object> slots = new ImmutableList.Builder<>();
            List<ItemStack> bases = brewingRecipeDisplay.base().getStacks(context);
            if (!bases.isEmpty()) {
                slots.add(RecipeAlternativesWidgetAccessor.AlternativeButtonWidgetAccessor.slot(0, 2, bases));
            }

            List<ItemStack> reagents = brewingRecipeDisplay.reagent().getStacks(context);
            if (!reagents.isEmpty()) {
                slots.add(RecipeAlternativesWidgetAccessor.AlternativeButtonWidgetAccessor.slot(1, 0, reagents));
            }

            info.setReturnValue(slots.build());
        }

        @Unique
        private Identifier getBrewingStandOverlayTexture(boolean enabled) {
            if (enabled) {
                return this.isSelected() ? BREWING_TEXTURE_ENABLED_HIGHLIGHTED : BREWING_TEXTURE_ENABLED;
            }

            return this.isSelected() ? BREWING_TEXTURE_DISABLED_HIGHLIGHTED : BREWING_TEXTURE_DISABLED;
        }
    }
}
