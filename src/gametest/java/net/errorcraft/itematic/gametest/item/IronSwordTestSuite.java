package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
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
    public void attackingEntityWhileInSurvivalModeDamagesIronSword(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemKeys.IRON_SWORD));
        Pig pig = TestUtil.createEntity(context, EntityType.PIG, entity -> {});
        context.succeedIf(() -> {
            player.attack(pig);
            Assert.itemStack(context, player.getMainHandItem())
                .isDamaged();
        });
    }

    @GameTest
    @SuppressWarnings("removal")
    public void attackingEntityWhileInCreativeModeDoesNotDamageIronSword(GameTestHelper context) {
        ServerPlayer player = context.makeMockServerPlayerInLevel();
        ServerLevel world = context.getLevel();
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemKeys.IRON_SWORD));
        Pig pig = TestUtil.createEntity(context, EntityType.PIG, entity -> {});
        context.succeedIf(() -> {
            player.attack(pig);
            Assert.itemStack(context, player.getMainHandItem())
                .isNotDamaged();
        });
    }
}
