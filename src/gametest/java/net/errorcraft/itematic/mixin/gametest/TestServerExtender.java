package net.errorcraft.itematic.mixin.gametest;

import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureManager;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.test.TestServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TestServer.class)
public class TestServerExtender {
    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/resource/featuretoggle/FeatureManager;getFeatureSet()Lnet/minecraft/resource/featuretoggle/FeatureSet;"
        )
    )
    private static FeatureSet doNotUseExperimentalFeatures(FeatureManager instance) {
        return FeatureFlags.VANILLA_FEATURES;
    }

    @ModifyArg(
        method = "create",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/SaveLoading$DataPacks;<init>(Lnet/minecraft/resource/ResourcePackManager;Lnet/minecraft/resource/DataConfiguration;ZZ)V"
        ),
        index = 3
    )
    private static boolean doNotUseExperimentalFeatures(boolean initMode) {
        return false;
    }
}
