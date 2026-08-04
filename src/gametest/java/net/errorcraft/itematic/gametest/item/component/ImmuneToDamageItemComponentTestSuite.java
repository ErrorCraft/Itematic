package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.item.ItemKeys;
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
    public void explodingNetherStarKeepsItemAlive(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemEntity netherStar = new ItemEntity(
            world,
            0.0d,
            0.0d,
            0.0d,
            world.itematic$createStack(ItemKeys.NETHER_STAR)
        );
        TestUtil.spawnEntity(context, netherStar, SPAWN_POSITION);
        netherStar.hurtServer(world, world.damageSources().explosion(null), Float.MAX_VALUE);
        context.succeedIf(() -> context.assertEntityPresent(EntityType.ITEM));
    }

    @GameTest
    public void explodingStickDestroysItem(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemEntity stick = new ItemEntity(
            world,
            0.0d,
            0.0d,
            0.0d,
            world.itematic$createStack(ItemKeys.STICK)
        );
        TestUtil.spawnEntity(context, stick, SPAWN_POSITION);
        stick.hurtServer(world, world.damageSources().explosion(null), Float.MAX_VALUE);
        context.succeedIf(() -> context.assertEntityNotPresent(EntityType.ITEM));
    }

    @GameTest
    public void settingNetheriteIngotOnFireKeepsItemAlive(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemEntity netheriteIngot = new ItemEntity(
            world,
            0.0d,
            0.0d,
            0.0d,
            world.itematic$createStack(ItemKeys.NETHERITE_INGOT)
        );
        TestUtil.spawnEntity(context, netheriteIngot, SPAWN_POSITION);
        netheriteIngot.hurtServer(world, world.damageSources().inFire(), Float.MAX_VALUE);
        context.succeedIf(() -> context.assertEntityPresent(EntityType.ITEM));
    }

    @GameTest
    public void settingStickOnFireDestroysItem(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        ItemEntity stick = new ItemEntity(
            world,
            0.0d,
            0.0d,
            0.0d,
            world.itematic$createStack(ItemKeys.STICK)
        );
        TestUtil.spawnEntity(context, stick, SPAWN_POSITION);
        stick.hurtServer(world, world.damageSources().inFire(), Float.MAX_VALUE);
        context.succeedIf(() -> context.assertEntityNotPresent(EntityType.ITEM));
    }
}
