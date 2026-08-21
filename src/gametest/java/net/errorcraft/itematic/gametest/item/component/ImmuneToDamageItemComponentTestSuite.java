package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;

public class ImmuneToDamageItemComponentTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest
    public void explodingNetherStarKeepsItemAlive(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemEntity netherStar = new ItemEntity(
            level,
            0.0d,
            0.0d,
            0.0d,
            level.itematic$createStack(ItemIds.NETHER_STAR)
        );
        TestUtil.spawnEntity(helper, netherStar, SPAWN_POSITION);
        netherStar.hurtServer(level, level.damageSources().explosion(null), Float.MAX_VALUE);
        helper.succeedIf(() -> helper.assertEntityPresent(EntityType.ITEM));
    }

    @GameTest
    public void explodingStickDestroysItem(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemEntity stick = new ItemEntity(
            level,
            0.0d,
            0.0d,
            0.0d,
            level.itematic$createStack(ItemIds.STICK)
        );
        TestUtil.spawnEntity(helper, stick, SPAWN_POSITION);
        stick.hurtServer(level, level.damageSources().explosion(null), Float.MAX_VALUE);
        helper.succeedIf(() -> helper.assertEntityNotPresent(EntityType.ITEM));
    }

    @GameTest
    public void settingNetheriteIngotOnFireKeepsItemAlive(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemEntity netheriteIngot = new ItemEntity(
            level,
            0.0d,
            0.0d,
            0.0d,
            level.itematic$createStack(ItemIds.NETHERITE_INGOT)
        );
        TestUtil.spawnEntity(helper, netheriteIngot, SPAWN_POSITION);
        netheriteIngot.hurtServer(level, level.damageSources().inFire(), Float.MAX_VALUE);
        helper.succeedIf(() -> helper.assertEntityPresent(EntityType.ITEM));
    }

    @GameTest
    public void settingStickOnFireDestroysItem(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemEntity stick = new ItemEntity(
            level,
            0.0d,
            0.0d,
            0.0d,
            level.itematic$createStack(ItemIds.STICK)
        );
        TestUtil.spawnEntity(helper, stick, SPAWN_POSITION);
        stick.hurtServer(level, level.damageSources().inFire(), Float.MAX_VALUE);
        helper.succeedIf(() -> helper.assertEntityNotPresent(EntityType.ITEM));
    }
}
