package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.recipe.brewing.BrewingRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeAlternativesWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(RecipeAlternativesWidget.class)
public interface RecipeAlternativesWidgetAccessor {
    @Accessor("client")
    MinecraftClient itematic$client();

    @Mixin(RecipeAlternativesWidget.AlternativeButtonWidget.class)
    abstract class AlternativeButtonWidgetExtender extends ClickableWidget {
        @Shadow
        @Final
        RecipeAlternativesWidget field_3113;

        @Shadow
        @Final
        private boolean craftable;

        @Shadow
        @Final
        protected List<RecipeAlternativesWidget.AlternativeButtonWidget.InputSlot> slots;

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

        public AlternativeButtonWidgetExtender(int x, int y, int width, int height, Text message) {
            super(x, y, width, height, message);
        }

        @Inject(
            method = "<init>",
            at = @At("TAIL")
        )
        private void setBrewingRecipe(RecipeAlternativesWidget parent, int x, int y, RecipeEntry<?> recipe, boolean craftable, CallbackInfo info) {
            this.isBrewingRecipe = recipe.value() instanceof BrewingRecipe<?>;
        }

        @WrapWithCondition(
            method = "alignRecipe",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeAlternativesWidget$AlternativeButtonWidget;alignRecipeToGrid(IIILnet/minecraft/recipe/RecipeEntry;Ljava/util/Iterator;I)V"
            )
        )
        private <T> boolean checkBrewingRecipe(RecipeAlternativesWidget.AlternativeButtonWidget instance, int gridWidth, int gridHeight, int gridOutputSlot, RecipeEntry<?> recipe, Iterator<T> inputs, int amount) {
            if (!(recipe.value() instanceof BrewingRecipe<?> brewingRecipe)) {
                return true;
            }

            ClientWorld world = ((RecipeAlternativesWidgetAccessor) this.field_3113).itematic$client().world;
            if (world == null) {
                return false;
            }

            DynamicRegistryManager registries = world.getRegistryManager();
            ItemStack[] bases = brewingRecipe.inputIngredient(registries.getWrapperOrThrow(RegistryKeys.ITEM))
                .itematic$getMatchingStacks(registries);
            if (bases.length > 0) {
                this.slots.add(InputSlotAccessor.create(instance, 3, 17, bases));
            }

            ItemStack[] reagents = brewingRecipe.reagent().itematic$getMatchingStacks(registries);
            if (reagents.length > 0) {
                this.slots.add(InputSlotAccessor.create(instance, 10, 3, reagents));
            }

            return false;
        }

        @Redirect(
            method = "alignRecipe",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/recipe/Recipe;getIngredients()Lnet/minecraft/util/collection/DefaultedList;"
            )
        )
        @SuppressWarnings("DataFlowIssue")
        private DefaultedList<Ingredient> passItemLookup(Recipe<?> instance) {
            return ((RecipeAccess) instance).itematic$ingredients(
                ((RecipeAlternativesWidgetAccessor) this.field_3113).itematic$client().world
                    .getRegistryManager()
                    .getWrapperOrThrow(RegistryKeys.ITEM)
            );
        }

        @Redirect(
            method = "acceptAlignedInput(Lnet/minecraft/recipe/Ingredient;IIII)V",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/recipe/Ingredient;getMatchingStacks()[Lnet/minecraft/item/ItemStack;"
            )
        )
        private ItemStack[] getMatchingStacksUseDynamicRegistry(Ingredient instance) {
            ClientWorld world = ((RecipeAlternativesWidgetAccessor) this.field_3113).itematic$client().world;
            if (world == null) {
                return new ItemStack[0];
            }

            return instance.itematic$getMatchingStacks(world.getRegistryManager());
        }

        @ModifyArg(
            method = "renderWidget",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"
            )
        )
        private Identifier getBrewingStandOverlayTexture(Identifier texture) {
            if (!this.isBrewingRecipe) {
                return texture;
            }

            if (this.craftable) {
                return this.isSelected() ? BREWING_TEXTURE_ENABLED_HIGHLIGHTED : BREWING_TEXTURE_ENABLED;
            }

            return this.isSelected() ? BREWING_TEXTURE_DISABLED_HIGHLIGHTED : BREWING_TEXTURE_DISABLED;
        }

        @Mixin(RecipeAlternativesWidget.AlternativeButtonWidget.InputSlot.class)
        interface InputSlotAccessor {
            @Invoker("<init>")
            static RecipeAlternativesWidget.AlternativeButtonWidget.InputSlot create(RecipeAlternativesWidget.AlternativeButtonWidget widget, final int y, final int x, final ItemStack[] stacks) {
                throw new AssertionError();
            }
        }
    }

    @Mixin(targets = "net.minecraft.client.gui.screen.recipebook.RecipeAlternativesWidget$FurnaceAlternativeButtonWidget")
    class FurnaceAlternativeButtonWidgetExtender {
        @Redirect(
            method = "alignRecipe",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/recipe/Ingredient;getMatchingStacks()[Lnet/minecraft/item/ItemStack;"
            )
        )
        private ItemStack[] getMatchingStacksUseDynamicRegistry(Ingredient instance) {
            ClientWorld world = MinecraftClient.getInstance().world;
            if (world == null) {
                return new ItemStack[0];
            }

            return instance.itematic$getMatchingStacks(world.getRegistryManager());
        }
    }
}
