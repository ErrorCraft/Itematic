package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.errorcraft.itematic.access.client.gui.screen.recipebook.RecipeBookWidgetAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetExtender implements RecipeBookWidgetAccess {
    @Inject(
        method = "initialize",
        at = @At("TAIL")
    )
    private void initializeAllowRecipeSpecificInitialization(int parentWidth, int parentHeight, MinecraftClient client, boolean narrow, AbstractRecipeScreenHandler<?> craftingScreenHandler, CallbackInfo info) {
        this.itematic$initializeRecipeSpecific(client.world);
    }
}
