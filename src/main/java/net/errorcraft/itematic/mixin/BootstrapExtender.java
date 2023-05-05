package net.errorcraft.itematic.mixin;

import net.minecraft.Bootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Bootstrap.class)
public class BootstrapExtender {
    @Redirect(
        method = "initialize",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/ComposterBlock;registerDefaultCompostableItems()V"
        )
    )
    private static void doNotRegisterCompostableItems() {}
}
