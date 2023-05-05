package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.AbstractFurnaceRecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceScreen.class)
public class AbstractFurnaceScreenExtender<T extends AbstractFurnaceScreenHandler> {
    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void initSetRecipeBookDynamicRegistryManager(AbstractFurnaceScreenHandler handler, AbstractFurnaceRecipeBookScreen recipeBook, PlayerInventory inventory, Text title, Identifier background, CallbackInfo info) {
        recipeBook.setRegistryManager(inventory.player.getWorld().getRegistryManager());
    }
}
