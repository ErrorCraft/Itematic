package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

public class ImmuneToDamageItemComponentTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void explodingNetherStarKeepsItemAlive(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.NETHER_STAR);
        ItemEntity item = new ItemEntity(world, 0.0d, 0.0d, 0.0d, stack);
        TestUtil.spawnEntity(context, item, SPAWN_POSITION);
        item.damage(world.getDamageSources().explosion(null), Float.MAX_VALUE);
        context.addInstantFinalTask(() -> context.expectEntity(EntityType.ITEM));
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void explodingStickDestroysItem(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.STICK);
        ItemEntity item = new ItemEntity(world, 0.0d, 0.0d, 0.0d, stack);
        TestUtil.spawnEntity(context, item, SPAWN_POSITION);
        item.damage(world.getDamageSources().explosion(null), Float.MAX_VALUE);
        context.addInstantFinalTask(() -> context.dontExpectEntity(EntityType.ITEM));
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void firingNetheriteIngotKeepsItemAlive(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.NETHERITE_INGOT);
        ItemEntity item = new ItemEntity(world, 0.0d, 0.0d, 0.0d, stack);
        TestUtil.spawnEntity(context, item, SPAWN_POSITION);
        item.damage(world.getDamageSources().inFire(), Float.MAX_VALUE);
        context.addInstantFinalTask(() -> context.expectEntity(EntityType.ITEM));
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void firingStickDestroysItem(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.STICK);
        ItemEntity item = new ItemEntity(world, 0.0d, 0.0d, 0.0d, stack);
        TestUtil.spawnEntity(context, item, SPAWN_POSITION);
        item.damage(world.getDamageSources().inFire(), Float.MAX_VALUE);
        context.addInstantFinalTask(() -> context.dontExpectEntity(EntityType.ITEM));
    }
}
