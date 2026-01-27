package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.screen.recipebook.RecipeBookGhostSlots;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class RecipeBookGhostSlotsExtender {
//    @Mixin(RecipeBookGhostSlots.GhostInputSlot.class)
//    public static class GhostInputSlotExtender {
//        @Redirect(
//            method = "getCurrentItemStack",
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
