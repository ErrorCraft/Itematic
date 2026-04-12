package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

public class ImmuneToDamageItemComponentTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest
    public void explodingNetherStarKeepsItemAlive(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemEntity netherStar = new ItemEntity(
            world,
            0.0d,
            0.0d,
            0.0d,
            world.itematic$createStack(ItemKeys.NETHER_STAR)
        );
        TestUtil.spawnEntity(context, netherStar, SPAWN_POSITION);
        netherStar.damage(world, world.getDamageSources().explosion(null), Float.MAX_VALUE);
        context.addFinalTask(() -> context.expectEntity(EntityType.ITEM));
    }

    @GameTest
    public void explodingStickDestroysItem(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemEntity stick = new ItemEntity(
            world,
            0.0d,
            0.0d,
            0.0d,
            world.itematic$createStack(ItemKeys.STICK)
        );
        TestUtil.spawnEntity(context, stick, SPAWN_POSITION);
        stick.damage(world, world.getDamageSources().explosion(null), Float.MAX_VALUE);
        context.addFinalTask(() -> context.dontExpectEntity(EntityType.ITEM));
    }

    @GameTest
    public void settingNetheriteIngotOnFireKeepsItemAlive(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemEntity netheriteIngot = new ItemEntity(
            world,
            0.0d,
            0.0d,
            0.0d,
            world.itematic$createStack(ItemKeys.NETHERITE_INGOT)
        );
        TestUtil.spawnEntity(context, netheriteIngot, SPAWN_POSITION);
        netheriteIngot.damage(world, world.getDamageSources().inFire(), Float.MAX_VALUE);
        context.addFinalTask(() -> context.expectEntity(EntityType.ITEM));
    }

    @GameTest
    public void settingStickOnFireDestroysItem(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemEntity stick = new ItemEntity(
            world,
            0.0d,
            0.0d,
            0.0d,
            world.itematic$createStack(ItemKeys.STICK)
        );
        TestUtil.spawnEntity(context, stick, SPAWN_POSITION);
        stick.damage(world, world.getDamageSources().inFire(), Float.MAX_VALUE);
        context.addFinalTask(() -> context.dontExpectEntity(EntityType.ITEM));
    }
}
