package net.errorcraft.itematic.mixin.gametest.framework;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameTestHelper.class)
public abstract class GameTestHelperExtender {
    @Shadow
    @Final
    private GameTestInfo testInfo;

    @Shadow
    public abstract Vec3 absoluteVec(Vec3 pos);

    @ModifyReturnValue(
        method = "makeMockPlayer",
        at = @At("TAIL")
    )
    private Player setPlayerData(Player original, GameType gameMode) {
        original.setPos(this.absoluteVec(Vec3.ZERO));
        gameMode.updatePlayerAbilities(original.getAbilities());
        return original;
    }

    @ModifyReturnValue(
        method = "makeMockServerPlayerInLevel",
        at = @At("TAIL")
    )
    private ServerPlayer removePlayerWhenFinished(ServerPlayer original) {
        this.testInfo.itematic$whenFinished(() -> original.connection.disconnect(Component.empty()));
        return original;
    }
}
