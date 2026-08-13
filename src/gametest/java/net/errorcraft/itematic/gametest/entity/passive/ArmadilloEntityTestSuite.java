package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.player.Player;
import java.util.Optional;

public class ArmadilloEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    @SuppressWarnings("removal")
    public void holdingSpiderEyeTemptsArmadillo(GameTestHelper context) {
        Armadillo armadillo = context.spawn(EntityType.ARMADILLO, SPAWN_POSITION);
        ServerPlayer player = context.makeMockServerPlayerInLevel();
        player.setItemInHand(InteractionHand.MAIN_HAND, context.getLevel().itematic$createStack(ItemIds.SPIDER_EYE));
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        context.succeedWhen(() -> {
            Optional<Player> temptingPlayer = armadillo.getBrain().getMemory(MemoryModuleType.TEMPTING_PLAYER);
            Assert.isTrue(context, temptingPlayer.isPresent(), () -> "Armadillo was not tempted by a Player");
            Assert.areEqual(context, temptingPlayer.get(), player, "Armadillo was not tempted by the expected Player");
        });
    }
}
