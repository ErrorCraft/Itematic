package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeAlternativesWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeAlternativesWidget.class)
public interface RecipeAlternativesWidgetAccessor {
    @Accessor("client")
    MinecraftClient itematic$client();

    @Mixin(targets = "net.minecraft.client.gui.screen.recipebook.RecipeAlternativesWidget$AlternativeButtonWidget")
    class AlternativeButtonWidgetExtender {
        @Shadow
        @Final
        RecipeAlternativesWidget field_3113;

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
