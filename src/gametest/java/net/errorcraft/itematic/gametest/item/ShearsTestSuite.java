package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class ShearsTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.shears.platform")
    public void usingShearsOnSaddledPigRemovesSaddle(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack shears = world.itematic$createStack(ItemKeys.SHEARS);
        player.setStackInHand(Hand.MAIN_HAND, shears);
        PigEntity target = context.spawnEntity(EntityType.PIG, SPAWN_POSITION);
        target.equipStack(EquipmentSlot.SADDLE, world.itematic$createStack(ItemKeys.SADDLE));
        context.addFinalTask(() -> {
            ActionResult result = target.interact(player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Shears usage on Pig to be successful"
            );
            Assert.itemStack(context, shears)
                .isDamaged();
            Assert.isFalse(
                context,
                target.hasSaddleEquipped(),
                () -> "Expected Pig not to be saddled"
            );
            Assert.entityType(context, EntityType.ITEM)
                .exists(
                    Assert::itemEntity,
                    itemEntity -> itemEntity.itemStack(heldStack -> heldStack
                        .is(ItemKeys.SADDLE))
                );
        });
    }

    @GameTest(structure = "itematic:item.shears.platform")
    public void usingShearsOnSaddledHorseRemovesSaddle(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack shears = world.itematic$createStack(ItemKeys.SHEARS);
        player.setStackInHand(Hand.MAIN_HAND, shears);
        HorseEntity target = context.spawnEntity(EntityType.HORSE, SPAWN_POSITION);
        target.equipStack(EquipmentSlot.SADDLE, world.itematic$createStack(ItemKeys.SADDLE));
        context.addFinalTask(() -> {
            ActionResult result = target.interact(player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Shears usage on Horse to be successful"
            );
            Assert.itemStack(context, shears)
                .isDamaged();
            Assert.isFalse(
                context,
                target.hasSaddleEquipped(),
                () -> "Expected Horse not to be saddled"
            );
            Assert.entityType(context, EntityType.ITEM)
                .exists(
                    Assert::itemEntity,
                    itemEntity -> itemEntity.itemStack(heldStack -> heldStack
                        .is(ItemKeys.SADDLE))
                );
        });
    }

    @GameTest(structure = "itematic:item.shears.platform")
    public void usingShearsOnHorseWithHorseArmorRemovesHorseArmor(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack shears = world.itematic$createStack(ItemKeys.SHEARS);
        player.setStackInHand(Hand.MAIN_HAND, shears);
        HorseEntity target = context.spawnEntity(EntityType.HORSE, SPAWN_POSITION);
        target.equipStack(EquipmentSlot.BODY, world.itematic$createStack(ItemKeys.IRON_HORSE_ARMOR));
        context.addFinalTask(() -> {
            ActionResult result = target.interact(player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Shears usage on Horse to be successful"
            );
            Assert.itemStack(context, shears)
                .isDamaged();
            Assert.isFalse(
                context,
                target.isWearingBodyArmor(),
                () -> "Expected Horse not to be wearing Horse Armor"
            );
            Assert.entityType(context, EntityType.ITEM)
                .exists(
                    Assert::itemEntity,
                    itemEntity -> itemEntity.itemStack(heldStack -> heldStack
                        .is(ItemKeys.IRON_HORSE_ARMOR))
                );
        });
    }

    @GameTest(structure = "itematic:item.shears.platform")
    public void usingShearsOnWolfWithCorrectOwnerRemovesWolfArmor(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack shears = world.itematic$createStack(ItemKeys.SHEARS);
        player.setStackInHand(Hand.MAIN_HAND, shears);
        WolfEntity target = context.spawnEntity(EntityType.WOLF, SPAWN_POSITION);
        target.equipStack(EquipmentSlot.BODY, world.itematic$createStack(ItemKeys.WOLF_ARMOR));
        target.setOwner(player);
        context.addFinalTask(() -> {
            ActionResult result = target.interact(player, Hand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.isAccepted(),
                () -> "Expected Shears usage on Wolf to be successful"
            );
            Assert.itemStack(context, shears)
                .isDamaged();
            Assert.isFalse(
                context,
                target.isWearingBodyArmor(),
                () -> "Expected Wolf not to be wearing Wolf Armor"
            );
            Assert.entityType(context, EntityType.ITEM)
                .exists(
                    Assert::itemEntity,
                    itemEntity -> itemEntity.itemStack(heldStack -> heldStack
                        .is(ItemKeys.WOLF_ARMOR))
                );
        });
    }

    @GameTest(structure = "itematic:item.shears.platform")
    public void usingShearsOnWolfWithIncorrectOwnerDoesNotRemoveWolfArmor(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack shears = world.itematic$createStack(ItemKeys.SHEARS);
        player.setStackInHand(Hand.MAIN_HAND, shears);
        WolfEntity target = context.spawnEntity(EntityType.WOLF, SPAWN_POSITION);
        target.equipStack(EquipmentSlot.BODY, world.itematic$createStack(ItemKeys.WOLF_ARMOR));
        context.addFinalTask(() -> {
            ActionResult result = target.interact(player, Hand.MAIN_HAND);
            Assert.isFalse(
                context,
                result.isAccepted(),
                () -> "Expected Shears usage on Wolf to be unsuccessful"
            );
            Assert.itemStack(context, shears)
                .isNotDamaged();
            Assert.isTrue(
                context,
                target.isWearingBodyArmor(),
                () -> "Expected Wolf to be wearing Wolf Armor"
            );
            Assert.entityType(context, EntityType.ITEM)
                .doesNotExist();
        });
    }
}
