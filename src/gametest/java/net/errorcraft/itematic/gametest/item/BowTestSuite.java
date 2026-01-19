package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class BowTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 2, 1);

    @GameTest(templateName = "itematic:item.bow.platform")
    public void usingBowWithMultishotSpawnsMultipleArrows(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BOW);
        stack.addEnchantment(
            world.getRegistryManager()
                .getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
                .getOrThrow(Enchantments.MULTISHOT),
            1
        );
        ItemStack ammunition = world.itematic$createStack(ItemKeys.ARROW);
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        player.getInventory().insertStack(ammunition);
        world.spawnEntity(player);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> stack.use(world, player, Hand.MAIN_HAND))
            .expectMinDurationAndRun(20, () -> {
                player.stopUsingItem();
                context.expectEntities(EntityType.ARROW, 3);
            })
            .completeIfSuccessful();
    }
}
