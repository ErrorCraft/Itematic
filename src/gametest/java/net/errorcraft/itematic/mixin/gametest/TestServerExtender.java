package net.errorcraft.itematic.mixin.gametest;

import net.minecraft.gametest.framework.GameTestServer;
import net.minecraft.world.flag.FeatureFlagRegistry;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameTestServer.class)
public class TestServerExtender {
    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/flag/FeatureFlagRegistry;allFlags()Lnet/minecraft/world/flag/FeatureFlagSet;"
        )
    )
    private static FeatureFlagSet doNotUseExperimentalFeatures(FeatureFlagRegistry instance) {
        return FeatureFlags.VANILLA_SET;
    }

    @ModifyArg(
        method = "create",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/WorldLoader$PackConfig;<init>(Lnet/minecraft/server/packs/repository/PackRepository;Lnet/minecraft/world/level/WorldDataConfiguration;ZZ)V"
        ),
        index = 3
    )
    private static boolean doNotUseExperimentalFeatures(boolean initMode) {
        return false;
    }
}
