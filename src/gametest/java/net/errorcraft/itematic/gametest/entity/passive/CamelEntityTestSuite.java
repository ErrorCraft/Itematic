package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class CamelEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 2, 1);

    @GameTest(templateName = "itematic:entity.platform")
    @SuppressWarnings("removal")
    public void holdingCactusTemptsCamel(TestContext context) {
        CamelEntity camel = context.spawnEntity(EntityType.CAMEL, SPAWN_POSITION);
        ServerPlayerEntity player = context.createMockCreativeServerPlayerInWorld();
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CACTUS);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        context.addInstantFinalTask(() -> {
            Optional<PlayerEntity> temptingPlayer = camel.getBrain().getOptionalRegisteredMemory(MemoryModuleType.TEMPTING_PLAYER);
            if (temptingPlayer.isEmpty()) {
                throw new GameTestException("Camel was not tempted by a player");
            }
            if (temptingPlayer.get() != player) {
                throw new GameTestException("Camel was not tempted by the expected player");
            }
        });
    }
}
