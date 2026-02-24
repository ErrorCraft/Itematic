package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.errorcraft.itematic.client.gui.screen.ingame.BrewingStandScreenDelegate;
import net.errorcraft.itematic.screen.BrewingStandMenuDelegate;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.BrewingStandScreenHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(HandledScreens.class)
public class HandledScreensExtender {
    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreens;register(Lnet/minecraft/screen/ScreenHandlerType;Lnet/minecraft/client/gui/screen/ingame/HandledScreens$Provider;)V",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/screen/ScreenHandlerType;BREWING_STAND:Lnet/minecraft/screen/ScreenHandlerType;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static HandledScreens.Provider<BrewingStandMenuDelegate, BrewingStandScreenDelegate> useDelegate(HandledScreens.Provider<BrewingStandScreenHandler, BrewingStandScreen> provider) {
         return (handler, inventory, title) -> new BrewingStandScreenDelegate(handler, inventory, title, provider.create(handler.delegate(), inventory, title));
    }
}
