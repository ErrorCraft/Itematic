package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenExtender {
    @Shadow
    @Final
    private RecipeBookWidget recipeBook;

    @Inject(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;recipeBook:Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;",
            shift = At.Shift.AFTER
        )
    )
    private void initSetRecipeBookDynamicRegistryManager(PlayerEntity player, CallbackInfo info) {
        this.recipeBook.setRegistryManager(player.getWorld().getRegistryManager());
    }
}
