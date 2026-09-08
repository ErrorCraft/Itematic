package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

public class PotionTestSuite {
    private static final BlockPos DIRT_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:block.dirt")
    public void usingWaterBottleOnDirtTurnsItIntoMud(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            PotionContentsUtil.setPotion(
                helper.getLevel().itematic$createStack(ItemIds.POTION),
                Potions.WATER
            )
        );
        helper.useBlock(DIRT_POSITION, player);
        helper.succeedIf(() -> {
            Assert.blockState(helper, DIRT_POSITION)
                .is(Blocks.MUD);
            Assert.itemStack(helper, player.getItemInHand(InteractionHand.MAIN_HAND))
                .is(ItemIds.GLASS_BOTTLE);
        });
    }
}
