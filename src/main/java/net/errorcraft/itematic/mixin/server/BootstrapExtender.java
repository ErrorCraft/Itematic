package net.errorcraft.itematic.mixin.server;

import net.minecraft.server.Bootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Bootstrap.class)
public class BootstrapExtender {
    @Redirect(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/ComposterBlock;bootStrap()V"
        )
    )
    private static void doNotRegisterCompostableItems() {}

    @Redirect(
        method = "bootStrap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/dispenser/DispenseItemBehavior;bootStrap()V"
        )
    )
    private static void doNotRegisterDispenserBehaviors() {}
}
