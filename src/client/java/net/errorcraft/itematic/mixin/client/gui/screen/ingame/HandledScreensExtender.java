package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.errorcraft.itematic.client.gui.screen.ingame.BrewingStandScreenDelegate;
import net.errorcraft.itematic.world.inventory.BrewingStandMenuDelegate;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.world.inventory.BrewingStandMenu;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MenuScreens.class)
public class HandledScreensExtender {
    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/MenuScreens;register(Lnet/minecraft/world/inventory/MenuType;Lnet/minecraft/client/gui/screens/MenuScreens$ScreenConstructor;)V",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/inventory/MenuType;BREWING_STAND:Lnet/minecraft/world/inventory/MenuType;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static MenuScreens.ScreenConstructor<BrewingStandMenuDelegate, BrewingStandScreenDelegate> useDelegate(MenuScreens.ScreenConstructor<BrewingStandMenu, BrewingStandScreen> provider) {
         return (handler, inventory, title) -> new BrewingStandScreenDelegate(handler, inventory, title, provider.create(handler.delegate(), inventory, title));
    }
}
