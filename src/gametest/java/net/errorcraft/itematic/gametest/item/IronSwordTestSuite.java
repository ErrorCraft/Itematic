package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class IronSwordTestSuite {
    @GameTest
    public void attackingEntityWhileInSurvivalModeDamagesIronSword(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel level = helper.getLevel();
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.IRON_SWORD)
        );
        Pig pig = TestUtil.createEntity(helper, EntityType.PIG, entity -> {
        });
        helper.succeedIf(() -> {
            player.attack(pig);
            Assert.itemStack(helper, player.getMainHandItem())
                .isDamaged();
        });
    }

    @GameTest
    @SuppressWarnings("removal")
    public void attackingEntityWhileInCreativeModeDoesNotDamageIronSword(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        ServerLevel level = helper.getLevel();
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.IRON_SWORD)
        );
        Pig pig = TestUtil.createEntity(helper, EntityType.PIG, entity -> {
        });
        helper.succeedIf(() -> {
            player.attack(pig);
            Assert.itemStack(helper, player.getMainHandItem())
                .isNotDamaged();
        });
    }
}
