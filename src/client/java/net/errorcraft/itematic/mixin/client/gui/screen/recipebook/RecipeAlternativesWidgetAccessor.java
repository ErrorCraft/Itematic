package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeAlternativesWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeAlternativesWidget.class)
public interface RecipeAlternativesWidgetAccessor {
//    @Accessor("client")
//    MinecraftClient client();
//
//    @Mixin(targets = "net.minecraft.client.gui.screen.recipebook.RecipeAlternativesWidget$AlternativeButtonWidget")
//    class AlternativeButtonWidgetExtender {
//        @Shadow
//        @Final
//        RecipeAlternativesWidget field_3113;
//
//        @Redirect(
//            method = "acceptAlignedInput",
//            at = @At(
//                value = "INVOKE",
//                target = "Lnet/minecraft/recipe/Ingredient;getMatchingStacks()[Lnet/minecraft/item/ItemStack;"
//            )
//        )
//        private ItemStack[] getMatchingStacksUseDynamicRegistry(Ingredient instance) {
//            ClientWorld world = ((RecipeAlternativesWidgetAccessor) this.field_3113).client().world;
//            if (world == null) {
//                return new ItemStack[0];
//            }
//            return instance.itematic$getMatchingStacks(world.getRegistryManager());
//        }
//    }
//
//    @Mixin(targets = "net.minecraft.client.gui.screen.recipebook.RecipeAlternativesWidget$FurnaceAlternativeButtonWidget")
//    class FurnaceAlternativeButtonWidgetExtender {
//        @Redirect(
//            method = "alignRecipe",
//            at = @At(
//                value = "INVOKE",
//                target = "Lnet/minecraft/recipe/Ingredient;getMatchingStacks()[Lnet/minecraft/item/ItemStack;"
//            )
//        )
//        private ItemStack[] getMatchingStacksUseDynamicRegistry(Ingredient instance) {
//            ClientWorld world = MinecraftClient.getInstance().world;
//            if (world == null) {
//                return new ItemStack[0];
//            }
//            return instance.itematic$getMatchingStacks(world.getRegistryManager());
//        }
//    }
}
