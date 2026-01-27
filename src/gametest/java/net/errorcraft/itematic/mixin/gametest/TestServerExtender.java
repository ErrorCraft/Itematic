package net.errorcraft.itematic.mixin.gametest;

import net.minecraft.test.TestServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TestServer.class)
public class TestServerExtender {
//    @ModifyArg(
//        method = "create",
//        at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraft/server/SaveLoading$DataPacks;<init>(Lnet/minecraft/resource/ResourcePackManager;Lnet/minecraft/resource/DataConfiguration;ZZ)V"
//        ),
//        index = 3
//    )
//    private static boolean doNotUseExperimentalFeatures(boolean initMode) {
//        return false;
//    }
}
