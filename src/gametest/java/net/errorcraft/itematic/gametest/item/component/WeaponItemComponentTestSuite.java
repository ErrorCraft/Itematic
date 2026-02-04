package net.errorcraft.itematic.gametest.item.component;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;

import java.util.Objects;

public class WeaponItemComponentTestSuite {
    private static final double MAX_HEALTH_VICTIM = 100.0d;

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void zombieAttackingUnarmedDealsDamageFromTrueBaseValueAttackDamageAttribute(TestContext context) {
        ServerWorld world = context.getWorld();
        ZombieEntity zombie = TestUtil.createEntity(context, EntityType.ZOMBIE, entity -> {});
        world.spawnEntity(zombie);
        PigEntity victim = spawnVictim(context);
        context.createTimedTaskRunner().expectMinDurationAndRun(1, () -> {
            context.assertTrue(zombie.tryAttack(victim), "Expected attack to be successful");
            Assert.areDoublesEqual(
                victim.getHealth(),
                MAX_HEALTH_VICTIM - zombie.getAttributeBaseValue(EntityAttributes.ATTACK_DAMAGE),
                (value, expected) -> "Expected health to be " + expected + ", got " + value + " instead"
            );
        }).completeIfSuccessful();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void zombieAttackingWithIronSwordDealsCorrectDamage(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.IRON_SWORD);
        ZombieEntity zombie = TestUtil.createEntity(context, EntityType.ZOMBIE, entity -> entity.setStackInHand(Hand.MAIN_HAND, stack));
        world.spawnEntity(zombie);
        PigEntity victim = spawnVictim(context);
        context.createTimedTaskRunner().expectMinDurationAndRun(1, () -> {
            context.assertTrue(zombie.tryAttack(victim), "Expected attack to be successful");
            Assert.areDoublesEqual(
                victim.getHealth(),
                MAX_HEALTH_VICTIM - zombie.getAttributeBaseValue(EntityAttributes.ATTACK_DAMAGE) - TestUtil.getItemBehavior(stack, ItemComponentTypes.WEAPON).attackDamage().defaultDamage(),
                (value, expected) -> "Expected health to be " + expected + ", got " + value + " instead"
            );
        }).completeIfSuccessful();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void piglinAttackingWithIronSwordDealsCorrectDamage(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.IRON_SWORD);
        PiglinEntity piglin = TestUtil.createEntity(context, EntityType.PIGLIN, entity -> entity.setStackInHand(Hand.MAIN_HAND, stack));
        world.spawnEntity(piglin);
        PigEntity victim = spawnVictim(context);
        context.createTimedTaskRunner().expectMinDurationAndRun(1, () -> {
            context.assertTrue(piglin.tryAttack(victim), "Expected attack to be successful");
            Assert.areDoublesEqual(
                victim.getHealth(),
                MAX_HEALTH_VICTIM - piglin.getAttributeBaseValue(EntityAttributes.ATTACK_DAMAGE) - TestUtil.getItemBehavior(stack, ItemComponentTypes.WEAPON).attackDamage().defaultDamage(),
                (value, expected) -> "Expected health to be " + expected + ", got " + value + " instead"
            );
        }).completeIfSuccessful();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void piglinAttackingWithGoldenSwordDealsCorrectDamage(TestContext context) {
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.GOLDEN_SWORD);
        PiglinEntity piglin = TestUtil.createEntity(context, EntityType.PIGLIN, entity -> entity.setStackInHand(Hand.MAIN_HAND, stack));
        world.spawnEntity(piglin);
        PigEntity victim = spawnVictim(context);
        context.createTimedTaskRunner().expectMinDurationAndRun(1, () -> {
            context.assertTrue(piglin.tryAttack(victim), "Expected attack to be successful");
            Assert.areDoublesEqual(
                victim.getHealth(),
                MAX_HEALTH_VICTIM - piglin.getAttributeBaseValue(EntityAttributes.ATTACK_DAMAGE) - TestUtil.getItemBehavior(stack, ItemComponentTypes.WEAPON).attackDamage().defaultDamage(),
                (value, expected) -> "Expected health to be " + expected + ", got " + value + " instead"
            );
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
