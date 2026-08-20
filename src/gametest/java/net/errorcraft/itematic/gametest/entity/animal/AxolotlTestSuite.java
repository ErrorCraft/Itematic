package net.errorcraft.itematic.gametest.entity.animal;

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
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class AxolotlTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    @SuppressWarnings("removal")
    public void holdingTropicalFishBucketTemptsAxolotl(GameTestHelper helper) {
        Axolotl axolotl = helper.spawn(EntityType.AXOLOTL, SPAWN_POSITION);
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            helper.getLevel().itematic$createStack(ItemIds.TROPICAL_FISH_BUCKET)
        );
        TestUtil.setEntityPos(helper, player, SPAWN_POSITION);
        helper.succeedWhen(() -> {
            Optional<Player> temptingPlayer = axolotl.getBrain().getMemory(MemoryModuleType.TEMPTING_PLAYER);
            Assert.isTrue(helper, temptingPlayer.isPresent(), () -> "Axolotl was not tempted by a Player");
            Assert.areEqual(helper, temptingPlayer.get(), player, () -> "Axolotl was not tempted by the expected Player");
        });
    }
}
