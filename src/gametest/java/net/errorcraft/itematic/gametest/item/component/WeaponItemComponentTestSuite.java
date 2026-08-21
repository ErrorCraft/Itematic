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
    public void zombieAttackingUnarmedDealsDamageFromTrueBaseValueAttackDamageAttribute(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Zombie zombie = TestUtil.createEntity(helper, EntityType.ZOMBIE, entity -> {
        });
        level.addFreshEntity(zombie);
        Pig victim = spawnVictim(helper);
        helper.startSequence().thenExecuteAfter(1, () -> {
            Assert.isTrue(
                helper,
                zombie.doHurtTarget(level, victim),
                () -> "Expected attack to be successful"
            );
            Assert.doubles(helper, victim.getHealth(), "health")
                .equals(MAX_HEALTH_VICTIM - zombie.itematic$getAttackDamage());
        }).thenSucceed();
    }

    @GameTest
    public void zombieAttackingWithIronSwordDealsCorrectDamage(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Zombie zombie = TestUtil.createEntity(
            helper,
            EntityType.ZOMBIE,
            entity -> entity.setItemInHand(
                InteractionHand.MAIN_HAND,
                level.itematic$createStack(ItemIds.IRON_SWORD)
            )
        );
        level.addFreshEntity(zombie);
        Pig victim = spawnVictim(helper);
        helper.startSequence().thenExecuteAfter(1, () -> {
            Assert.isTrue(
                helper,
                zombie.doHurtTarget(level, victim),
                () -> "Expected attack to be successful"
            );
            Assert.doubles(helper, victim.getHealth(), "health")
                .equals(MAX_HEALTH_VICTIM - zombie.itematic$getAttackDamage());
        }).thenSucceed();
    }

    @GameTest
    public void piglinAttackingWithIronSwordDealsCorrectDamage(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Piglin piglin = TestUtil.createEntity(
            helper,
            EntityType.PIGLIN,
            entity -> level.itematic$createStack(ItemIds.IRON_SWORD)
        );
        level.addFreshEntity(piglin);
        Pig victim = spawnVictim(helper);
        helper.startSequence().thenExecuteAfter(1, () -> {
            Assert.isTrue(
                helper,
                piglin.doHurtTarget(level, victim),
                () -> "Expected attack to be successful"
            );
            Assert.doubles(helper, victim.getHealth(), "health")
                .equals(MAX_HEALTH_VICTIM - piglin.itematic$getAttackDamage());
        }).thenSucceed();
    }

    @GameTest
    public void piglinAttackingWithGoldenSwordDealsCorrectDamage(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Piglin piglin = TestUtil.createEntity(
            helper,
            EntityType.PIGLIN,
            entity -> entity.setItemInHand(
                InteractionHand.MAIN_HAND,
                level.itematic$createStack(ItemIds.GOLDEN_SWORD)
            )
        );
        level.addFreshEntity(piglin);
        Pig victim = spawnVictim(helper);
        helper.startSequence().thenExecuteAfter(1, () -> {
            Assert.isTrue(
                helper,
                piglin.doHurtTarget(level, victim),
                () -> "Expected attack to be successful"
            );
            Assert.doubles(helper, victim.getHealth(), "health")
                .equals(MAX_HEALTH_VICTIM - piglin.itematic$getAttackDamage());
        }).thenSucceed();
    }

    private static Pig spawnVictim(GameTestHelper helper) {
        Pig victim = TestUtil.createEntity(helper, EntityType.PIG, entity -> {
            Objects.requireNonNull(entity.getAttributes().getInstance(Attributes.MAX_HEALTH))
                .setBaseValue(MAX_HEALTH_VICTIM);
            entity.setHealth((float) MAX_HEALTH_VICTIM);
        });
        helper.getLevel().addFreshEntity(victim);
        return victim;
    }
}
