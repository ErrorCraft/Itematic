package net.errorcraft.itematic.mixin.client.gui.screens.inventory;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({
    CreativeModeInventoryScreen.class,
    InventoryScreen.class
})
public class GameModeInventoryScreensExtender {
    @ModifyExpressionValue(
        method = "init",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;hasInfiniteMaterials()Z"
        )
    )
    @SuppressWarnings("ConstantValue")
    private boolean alsoCheckNullSelectedTab(boolean original) {
        return original && CreativeModeInventoryScreenAccessor.selectedTab() != null;
    }
}
