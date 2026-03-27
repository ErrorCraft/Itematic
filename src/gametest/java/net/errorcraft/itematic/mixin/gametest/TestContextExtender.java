package net.errorcraft.itematic.mixin.gametest;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.errorcraft.itematic.access.gametest.GameTestStateAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTestState;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TestContext.class)
public class TestContextExtender {
    @Shadow
    @Final
    private GameTestState test;

    @ModifyReturnValue(
        method = "createMockPlayer",
        at = @At("TAIL")
    )
    private PlayerEntity setPlayerAbilities(PlayerEntity original, GameMode gameMode) {
        gameMode.setAbilities(original.getAbilities());
        return original;
    }

    @ModifyReturnValue(
        method = "createMockCreativeServerPlayerInWorld",
        at = @At("TAIL")
    )
    private ServerPlayerEntity removePlayerWhenFinished(ServerPlayerEntity original) {
        ((GameTestStateAccess) this.test).itematic$whenFinished(() -> original.networkHandler.disconnect(Text.empty()));
        return original;
    }
}
