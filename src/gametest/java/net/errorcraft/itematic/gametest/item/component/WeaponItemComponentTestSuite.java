package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.zombie.Zombie;
import java.util.Objects;

public class WeaponItemComponentTestSuite {
    private static final double MAX_HEALTH_VICTIM = 100.0d;

    @GameTest
    public void zombieAttackingUnarmedDealsDamageFromTrueBaseValueAttackDamageAttribute(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Zombie zombie = TestUtil.createEntity(context, EntityType.ZOMBIE, entity -> {});
        world.addFreshEntity(zombie);
        Pig victim = spawnVictim(context);
        context.startSequence().thenExecuteAfter(1, () -> {
            Assert.isTrue(
                context,
                zombie.doHurtTarget(world, victim),
                () -> "Expected attack to be successful"
            );
            Assert.doubles(context, victim.getHealth(), "health")
                .equals(MAX_HEALTH_VICTIM - zombie.itematic$getAttackDamage());
        }).thenSucceed();
    }

    @GameTest
    public void zombieAttackingWithIronSwordDealsCorrectDamage(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Zombie zombie = TestUtil.createEntity(
            context,
            EntityType.ZOMBIE,
            entity -> entity.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemIds.IRON_SWORD))
        );
        world.addFreshEntity(zombie);
        Pig victim = spawnVictim(context);
        context.startSequence().thenExecuteAfter(1, () -> {
            Assert.isTrue(
                context,
                zombie.doHurtTarget(world, victim),
                () -> "Expected attack to be successful"
            );
            Assert.doubles(context, victim.getHealth(), "health")
                .equals(MAX_HEALTH_VICTIM - zombie.itematic$getAttackDamage());
        }).thenSucceed();
    }

    @GameTest
    public void piglinAttackingWithIronSwordDealsCorrectDamage(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Piglin piglin = TestUtil.createEntity(
            context,
            EntityType.PIGLIN,
            entity -> world.itematic$createStack(ItemIds.IRON_SWORD)
        );
        world.addFreshEntity(piglin);
        Pig victim = spawnVictim(context);
        context.startSequence().thenExecuteAfter(1, () -> {
            Assert.isTrue(
                context,
                piglin.doHurtTarget(world, victim),
                () -> "Expected attack to be successful"
            );
            Assert.doubles(context, victim.getHealth(), "health")
                .equals(MAX_HEALTH_VICTIM - piglin.itematic$getAttackDamage());
        }).thenSucceed();
    }

    @GameTest
    public void piglinAttackingWithGoldenSwordDealsCorrectDamage(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Piglin piglin = TestUtil.createEntity(
            context,
            EntityType.PIGLIN,
            entity -> entity.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemIds.GOLDEN_SWORD))
        );
        world.addFreshEntity(piglin);
        Pig victim = spawnVictim(context);
        context.startSequence().thenExecuteAfter(1, () -> {
            Assert.isTrue(
                context,
                piglin.doHurtTarget(world, victim),
                () -> "Expected attack to be successful"
            );
            Assert.doubles(context, victim.getHealth(), "health")
                .equals(MAX_HEALTH_VICTIM - piglin.itematic$getAttackDamage());
        }).thenSucceed();
    }

    private static Pig spawnVictim(GameTestHelper context) {
        Pig victim = TestUtil.createEntity(context, EntityType.PIG, entity -> {
            Objects.requireNonNull(entity.getAttributes().getInstance(Attributes.MAX_HEALTH))
                .setBaseValue(MAX_HEALTH_VICTIM);
            entity.setHealth((float) MAX_HEALTH_VICTIM);
        });
        context.getLevel().addFreshEntity(victim);
        return victim;
    }
}
