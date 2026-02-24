package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.errorcraft.itematic.access.recipe.RecipeFinderAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.recipe.RecipeFinder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetExtender {
    @Shadow
    @Final
    private RecipeFinder recipeFinder;

    @Inject(
        method = "initialize",
        at = @At("HEAD")
    )
    private void recipeFinderSetWorld(int parentWidth, int parentHeight, MinecraftClient client, boolean narrow, CallbackInfo info) {
        ((RecipeFinderAccess) this.recipeFinder).itematic$setWorld(client.world);
    }
}
