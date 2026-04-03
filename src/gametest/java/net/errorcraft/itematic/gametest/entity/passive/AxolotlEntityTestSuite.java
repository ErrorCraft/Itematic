package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class AxolotlEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    @SuppressWarnings("removal")
    public void holdingTropicalFishBucketTemptsAxolotl(TestContext context) {
        AxolotlEntity axolotl = context.spawnEntity(EntityType.AXOLOTL, SPAWN_POSITION);
        ServerPlayerEntity player = context.createMockCreativeServerPlayerInWorld();
        player.setStackInHand(Hand.MAIN_HAND, context.getWorld().itematic$createStack(ItemKeys.TROPICAL_FISH_BUCKET));
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        context.addInstantFinalTask(() -> {
            Optional<PlayerEntity> temptingPlayer = axolotl.getBrain().getOptionalRegisteredMemory(MemoryModuleType.TEMPTING_PLAYER);
            Assert.isTrue(context, temptingPlayer.isPresent(), () -> "Axolotl was not tempted by a Player");
            Assert.areEqual(context, temptingPlayer.get(), player, "Axolotl was not tempted by the expected Player");
        });
    }
}
