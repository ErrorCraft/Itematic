package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class CamelEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    @SuppressWarnings("removal")
    public void holdingCactusTemptsCamel(TestContext context) {
        CamelEntity camel = context.spawnEntity(EntityType.CAMEL, SPAWN_POSITION);
        ServerPlayerEntity player = context.createMockCreativeServerPlayerInWorld();
        player.setStackInHand(Hand.MAIN_HAND, context.getWorld().itematic$createStack(ItemKeys.CACTUS));
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        context.addInstantFinalTask(() -> {
            Optional<PlayerEntity> temptingPlayer = camel.getBrain().getOptionalRegisteredMemory(MemoryModuleType.TEMPTING_PLAYER);
            Assert.isTrue(context, temptingPlayer.isPresent(), () -> "Camel was not tempted by a Player");
            Assert.areEqual(context, temptingPlayer.get(), player, "Camel was not tempted by the expected Player");
        });
    }
}
