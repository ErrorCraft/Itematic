package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;

public class BowTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.bow.platform")
    public void usingBowWithMultishotSpawnsMultipleArrows(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemStack bow = world.itematic$createStack(ItemKeys.BOW);
        bow.enchant(
            world.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.MULTISHOT),
            1
        );
        ItemStack ammunition = world.itematic$createStack(ItemKeys.ARROW);
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(context, player, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, bow);
        player.getInventory().add(ammunition);
        world.addFreshEntity(player);
        context.startSequence()
            .thenExecute(() -> bow.use(world, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(20, () -> {
                player.releaseUsingItem();
                context.assertEntitiesPresent(EntityType.ARROW, 3);
            })
            .thenSucceed();
    }
}
