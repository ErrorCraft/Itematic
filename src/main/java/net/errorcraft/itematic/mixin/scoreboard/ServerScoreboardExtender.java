package net.errorcraft.itematic.mixin.scoreboard;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.scoreboard.ScoreboardStateAccess;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.ScoreboardState;
import net.minecraft.scoreboard.ServerScoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerScoreboard.class)
public class ServerScoreboardExtender {
    @ModifyExpressionValue(
        method = "stateFromNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/scoreboard/ServerScoreboard;createState()Lnet/minecraft/scoreboard/ScoreboardState;"
        )
    )
    private ScoreboardState setLookup(ScoreboardState original, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        ((ScoreboardStateAccess) original).itematic$setLookup(lookup);
        return original;
    }
}
