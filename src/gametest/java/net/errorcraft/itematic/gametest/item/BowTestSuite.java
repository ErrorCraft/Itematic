package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.references.ItemIds;
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
    public void usingBowWithMultishotSpawnsMultipleArrows(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack bow = level.itematic$createStack(ItemIds.BOW);
        bow.enchant(
            level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.MULTISHOT),
            1
        );
        ItemStack ammunition = level.itematic$createStack(ItemIds.ARROW);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, SPAWN_POSITION);
        player.setItemInHand(InteractionHand.MAIN_HAND, bow);
        player.getInventory().add(ammunition);
        level.addFreshEntity(player);
        helper.startSequence()
            .thenExecute(() -> bow.use(level, player, InteractionHand.MAIN_HAND))
            .thenExecuteAfter(20, () -> {
                player.releaseUsingItem();
                helper.assertEntitiesPresent(EntityType.ARROW, 3);
            })
            .thenSucceed();
    }
}
