package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.item.ItemKeys;
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
    public void usingShearsOnSaddledPigRemovesSaddle(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack shears = world.itematic$createStack(ItemKeys.SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, shears);
        Pig target = context.spawn(EntityType.PIG, SPAWN_POSITION);
        target.setItemSlot(EquipmentSlot.SADDLE, world.itematic$createStack(ItemKeys.SADDLE));
        context.succeedIf(() -> {
            InteractionResult result = target.interact(player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.consumesAction(),
                () -> "Expected Shears usage on Pig to be successful"
            );
            Assert.itemStack(context, shears)
                .isDamaged();
            Assert.isFalse(
                context,
                target.isSaddled(),
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
    public void usingShearsOnSaddledHorseRemovesSaddle(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack shears = world.itematic$createStack(ItemKeys.SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, shears);
        Horse target = context.spawn(EntityType.HORSE, SPAWN_POSITION);
        target.setItemSlot(EquipmentSlot.SADDLE, world.itematic$createStack(ItemKeys.SADDLE));
        context.succeedIf(() -> {
            InteractionResult result = target.interact(player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.consumesAction(),
                () -> "Expected Shears usage on Horse to be successful"
            );
            Assert.itemStack(context, shears)
                .isDamaged();
            Assert.isFalse(
                context,
                target.isSaddled(),
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
    public void usingShearsOnHorseWithHorseArmorRemovesHorseArmor(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack shears = world.itematic$createStack(ItemKeys.SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, shears);
        Horse target = context.spawn(EntityType.HORSE, SPAWN_POSITION);
        target.setItemSlot(EquipmentSlot.BODY, world.itematic$createStack(ItemKeys.IRON_HORSE_ARMOR));
        context.succeedIf(() -> {
            InteractionResult result = target.interact(player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.consumesAction(),
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
    public void usingShearsOnWolfWithCorrectOwnerRemovesWolfArmor(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack shears = world.itematic$createStack(ItemKeys.SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, shears);
        Wolf target = context.spawn(EntityType.WOLF, SPAWN_POSITION);
        target.setItemSlot(EquipmentSlot.BODY, world.itematic$createStack(ItemKeys.WOLF_ARMOR));
        target.setOwner(player);
        context.succeedIf(() -> {
            InteractionResult result = target.interact(player, InteractionHand.MAIN_HAND);
            Assert.isTrue(
                context,
                result.consumesAction(),
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
    public void usingShearsOnWolfWithIncorrectOwnerDoesNotRemoveWolfArmor(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack shears = world.itematic$createStack(ItemKeys.SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, shears);
        Wolf target = context.spawn(EntityType.WOLF, SPAWN_POSITION);
        target.setItemSlot(EquipmentSlot.BODY, world.itematic$createStack(ItemKeys.WOLF_ARMOR));
        context.succeedIf(() -> {
            InteractionResult result = target.interact(player, InteractionHand.MAIN_HAND);
            Assert.isFalse(
                context,
                result.consumesAction(),
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
