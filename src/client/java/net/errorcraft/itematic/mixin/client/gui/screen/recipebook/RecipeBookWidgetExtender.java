package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.errorcraft.itematic.access.recipe.RecipeFinderAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.entity.player.StackedItemContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookComponent.class)
public class RecipeBookWidgetExtender {
    @Shadow
    @Final
    private StackedItemContents stackedContents;

    @Inject(
        method = "init",
        at = @At("HEAD")
    )
    private void recipeFinderSetWorld(int parentWidth, int parentHeight, Minecraft client, boolean narrow, CallbackInfo info) {
        ((RecipeFinderAccess) this.stackedContents).itematic$setWorld(client.level);
    }
}
