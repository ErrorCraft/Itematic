package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class ShearsTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:item.shears.platform")
    public void usingShearsOnSaddledPigRemovesSaddle(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack shears = level.itematic$createStack(ItemIds.SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, shears);
        Pig target = helper.spawn(EntityType.PIG, SPAWN_POSITION);
        target.setItemSlot(EquipmentSlot.SADDLE, level.itematic$createStack(ItemIds.SADDLE));
        helper.succeedIf(() -> {
            InteractionResult result = target.interact(player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Shears usage on Pig to be successful"
            );
            Assert.itemStack(helper, shears)
                .isDamaged();
            Assert.isFalse(
                helper,
                target.isSaddled(),
                () -> "Expected Pig not to be saddled"
            );
            Assert.entityType(helper, EntityType.ITEM)
                .exists(
                    Assert::itemEntity,
                    itemEntity -> itemEntity.itemStack(heldStack -> heldStack
                        .is(ItemIds.SADDLE))
                );
        });
    }

    @GameTest(structure = "itematic:item.shears.platform")
    public void usingShearsOnSaddledHorseRemovesSaddle(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack shears = level.itematic$createStack(ItemIds.SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, shears);
        Horse target = helper.spawn(EntityType.HORSE, SPAWN_POSITION);
        target.setItemSlot(EquipmentSlot.SADDLE, level.itematic$createStack(ItemIds.SADDLE));
        helper.succeedIf(() -> {
            InteractionResult result = target.interact(player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Shears usage on Horse to be successful"
            );
            Assert.itemStack(helper, shears)
                .isDamaged();
            Assert.isFalse(
                helper,
                target.isSaddled(),
                () -> "Expected Horse not to be saddled"
            );
            Assert.entityType(helper, EntityType.ITEM)
                .exists(
                    Assert::itemEntity,
                    itemEntity -> itemEntity.itemStack(heldStack -> heldStack
                        .is(ItemIds.SADDLE))
                );
        });
    }

    @GameTest(structure = "itematic:item.shears.platform")
    public void usingShearsOnHorseWithHorseArmorRemovesHorseArmor(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack shears = level.itematic$createStack(ItemIds.SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, shears);
        Horse target = helper.spawn(EntityType.HORSE, SPAWN_POSITION);
        target.setItemSlot(EquipmentSlot.BODY, level.itematic$createStack(ItemIds.IRON_HORSE_ARMOR));
        helper.succeedIf(() -> {
            InteractionResult result = target.interact(player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Shears usage on Horse to be successful"
            );
            Assert.itemStack(helper, shears)
                .isDamaged();
            Assert.isFalse(
                helper,
                target.isWearingBodyArmor(),
                () -> "Expected Horse not to be wearing Horse Armor"
            );
            Assert.entityType(helper, EntityType.ITEM)
                .exists(
                    Assert::itemEntity,
                    itemEntity -> itemEntity.itemStack(heldStack -> heldStack
                        .is(ItemIds.IRON_HORSE_ARMOR))
                );
        });
    }

    @GameTest(structure = "itematic:item.shears.platform")
    public void usingShearsOnWolfWithCorrectOwnerRemovesWolfArmor(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack shears = level.itematic$createStack(ItemIds.SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, shears);
        Wolf target = helper.spawn(EntityType.WOLF, SPAWN_POSITION);
        target.setItemSlot(EquipmentSlot.BODY, level.itematic$createStack(ItemIds.WOLF_ARMOR));
        target.setOwner(player);
        helper.succeedIf(() -> {
            InteractionResult result = target.interact(player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                helper,
                result.consumesAction(),
                () -> "Expected Shears usage on Wolf to be successful"
            );
            Assert.itemStack(helper, shears)
                .isDamaged();
            Assert.isFalse(
                helper,
                target.isWearingBodyArmor(),
                () -> "Expected Wolf not to be wearing Wolf Armor"
            );
            Assert.entityType(helper, EntityType.ITEM)
                .exists(
                    Assert::itemEntity,
                    itemEntity -> itemEntity.itemStack(heldStack -> heldStack
                        .is(ItemIds.WOLF_ARMOR))
                );
        });
    }

    @GameTest(structure = "itematic:item.shears.platform")
    public void usingShearsOnWolfWithIncorrectOwnerDoesNotRemoveWolfArmor(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack shears = level.itematic$createStack(ItemIds.SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, shears);
        Wolf target = helper.spawn(EntityType.WOLF, SPAWN_POSITION);
        target.setItemSlot(EquipmentSlot.BODY, level.itematic$createStack(ItemIds.WOLF_ARMOR));
        helper.succeedIf(() -> {
            InteractionResult result = target.interact(player, InteractionHand.MAIN_HAND);
            Assert.isFalse(
                helper,
                result.consumesAction(),
                () -> "Expected Shears usage on Wolf to be unsuccessful"
            );
            Assert.itemStack(helper, shears)
                .isNotDamaged();
            Assert.isTrue(
                helper,
                target.isWearingBodyArmor(),
                () -> "Expected Wolf to be wearing Wolf Armor"
            );
            Assert.entityType(helper, EntityType.ITEM)
                .doesNotExist();
        });
    }
}
