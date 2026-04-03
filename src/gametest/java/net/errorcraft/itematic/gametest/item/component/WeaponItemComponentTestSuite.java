package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;

import java.util.Objects;

public class WeaponItemComponentTestSuite {
    private static final double MAX_HEALTH_VICTIM = 100.0d;

    @GameTest
    public void zombieAttackingUnarmedDealsDamageFromTrueBaseValueAttackDamageAttribute(TestContext context) {
        ServerWorld world = context.getWorld();
        ZombieEntity zombie = TestUtil.createEntity(context, EntityType.ZOMBIE, entity -> {});
        world.spawnEntity(zombie);
        PigEntity victim = spawnVictim(context);
        context.createTimedTaskRunner().expectMinDurationAndRun(1, () -> {
            Assert.isTrue(
                context,
                zombie.tryAttack(world, victim),
                () -> "Expected attack to be successful"
            );
            Assert.doubles(context, victim.getHealth(), "health")
                .equals(MAX_HEALTH_VICTIM - zombie.itematic$getAttackDamage());
        }).completeIfSuccessful();
    }

    @GameTest
    public void zombieAttackingWithIronSwordDealsCorrectDamage(TestContext context) {
        ServerWorld world = context.getWorld();
        ZombieEntity zombie = TestUtil.createEntity(
            context,
            EntityType.ZOMBIE,
            entity -> entity.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.IRON_SWORD))
        );
        world.spawnEntity(zombie);
        PigEntity victim = spawnVictim(context);
        context.createTimedTaskRunner().expectMinDurationAndRun(1, () -> {
            Assert.isTrue(
                context,
                zombie.tryAttack(world, victim),
                () -> "Expected attack to be successful"
            );
            Assert.doubles(context, victim.getHealth(), "health")
                .equals(MAX_HEALTH_VICTIM - zombie.itematic$getAttackDamage());
        }).completeIfSuccessful();
    }

    @GameTest
    public void piglinAttackingWithIronSwordDealsCorrectDamage(TestContext context) {
        ServerWorld world = context.getWorld();
        PiglinEntity piglin = TestUtil.createEntity(
            context,
            EntityType.PIGLIN,
            entity -> world.itematic$createStack(ItemKeys.IRON_SWORD)
        );
        world.spawnEntity(piglin);
        PigEntity victim = spawnVictim(context);
        context.createTimedTaskRunner().expectMinDurationAndRun(1, () -> {
            Assert.isTrue(
                context,
                piglin.tryAttack(world, victim),
                () -> "Expected attack to be successful"
            );
            Assert.doubles(context, victim.getHealth(), "health")
                .equals(MAX_HEALTH_VICTIM - piglin.itematic$getAttackDamage());
        }).completeIfSuccessful();
    }

    @GameTest
    public void piglinAttackingWithGoldenSwordDealsCorrectDamage(TestContext context) {
        ServerWorld world = context.getWorld();
        PiglinEntity piglin = TestUtil.createEntity(
            context,
            EntityType.PIGLIN,
            entity -> entity.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.GOLDEN_SWORD))
        );
        world.spawnEntity(piglin);
        PigEntity victim = spawnVictim(context);
        context.createTimedTaskRunner().expectMinDurationAndRun(1, () -> {
            Assert.isTrue(
                context,
                piglin.tryAttack(world, victim),
                () -> "Expected attack to be successful"
            );
            Assert.doubles(context, victim.getHealth(), "health")
                .equals(MAX_HEALTH_VICTIM - piglin.itematic$getAttackDamage());
        }).completeIfSuccessful();
    }

    private static PigEntity spawnVictim(TestContext context) {
        PigEntity victim = TestUtil.createEntity(context, EntityType.PIG, entity -> {
            Objects.requireNonNull(entity.getAttributes().getCustomInstance(EntityAttributes.MAX_HEALTH))
                .setBaseValue(MAX_HEALTH_VICTIM);
            entity.setHealth((float) MAX_HEALTH_VICTIM);
        });
        context.getWorld().spawnEntity(victim);
        return victim;
    }
}
