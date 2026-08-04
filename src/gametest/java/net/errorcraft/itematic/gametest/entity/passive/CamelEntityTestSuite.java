package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.player.Player;
import java.util.Optional;

public class CamelEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    @SuppressWarnings("removal")
    public void holdingCactusTemptsCamel(GameTestHelper context) {
        Camel camel = context.spawn(EntityType.CAMEL, SPAWN_POSITION);
        ServerPlayer player = context.makeMockServerPlayerInLevel();
        player.setItemInHand(InteractionHand.MAIN_HAND, context.getLevel().itematic$createStack(ItemKeys.CACTUS));
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        context.succeedWhen(() -> {
            Optional<Player> temptingPlayer = camel.getBrain().getMemory(MemoryModuleType.TEMPTING_PLAYER);
            Assert.isTrue(context, temptingPlayer.isPresent(), () -> "Camel was not tempted by a Player");
            Assert.areEqual(context, temptingPlayer.get(), player, "Camel was not tempted by the expected Player");
        });
    }
}
