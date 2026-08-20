package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.entity.animal.equine.Mule;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public class DispenserBehaviorTestSuite {
    private static final BlockPos DISPENSER_POSITION = new BlockPos(2, 1, 3);
    private static final BlockPos BUTTON_POSITION = DISPENSER_POSITION.offset(0, 1, 0);
    private static final BlockPos OUTPUT_POSITION = DISPENSER_POSITION.offset(0, 0, -1);
    private static final BlockPos ABOVE_OUTPUT_POSITION = OUTPUT_POSITION.offset(0, 1, 0);

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingArrowSpawnsArrow(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.ARROW));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertEntityPresent(EntityType.ARROW);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingExperienceBottleSpawnsExperienceBottle(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.EXPERIENCE_BOTTLE));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertEntityPresent(EntityType.EXPERIENCE_BOTTLE);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingFireworkRocketSpawnsFireworkRocket(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.FIREWORK_ROCKET));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertEntityPresent(EntityType.FIREWORK_ROCKET);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingFireChargeSpawnsEntity(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.FIRE_CHARGE));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertEntityPresent(EntityType.SMALL_FIREBALL);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingPigSpawnEggSpawnsPig(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.PIG_SPAWN_EGG));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertEntityPresent(EntityType.PIG);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingArmorStandSpawnsArmorStand(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.ARMOR_STAND));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertEntityPresent(EntityType.ARMOR_STAND);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSpruceBoatSpawnsSpruceBoat(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.SPRUCE_BOAT));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertEntityPresent(EntityType.SPRUCE_BOAT, OUTPUT_POSITION);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.grass_block")
    public void dispensingBoneMealFertilizesBlock(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.BONE_MEAL));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(helper, ABOVE_OUTPUT_POSITION)
                    .isNot(Blocks.AIR);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingBoneMealOnInvalidBlockKeepsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.BONE_MEAL));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(helper, ABOVE_OUTPUT_POSITION)
                    .is(Blocks.AIR);
                helper.assertItemEntityNotPresent(level.itematic$getItem(ItemIds.BONE_MEAL).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.BONE_MEAL);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingEquipmentEquipsEntity(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, OUTPUT_POSITION);
        level.addFreshEntity(player);
        ItemStack stack = level.itematic$createStack(ItemIds.IRON_HELMET);
        blockEntity.insertItem(stack);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.itemStack(helper, player.getItemBySlot(EquipmentSlot.HEAD))
                    .is(ItemIds.IRON_HELMET);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingEquipmentWithNoEntityDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.IRON_HELMET));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.IRON_HELMET).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHeadEquipsEntity(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, OUTPUT_POSITION);
        level.addFreshEntity(player);
        ItemStack stack = level.itematic$createStack(ItemIds.SKELETON_SKULL);
        blockEntity.insertItem(stack);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.itemStack(helper, player.getItemBySlot(EquipmentSlot.HEAD))
                    .is(ItemIds.SKELETON_SKULL);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHeadWithNoEntityKeepsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.SKELETON_SKULL));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityNotPresent(level.itematic$getItem(ItemIds.SKELETON_SKULL).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.SKELETON_SKULL);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingWaterBucketPlacesWater(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.WATER_BUCKET));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.fluidState(helper, OUTPUT_POSITION)
                    .is(FluidTags.WATER);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.BUCKET);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingWaterBucketWithObstructedBlockDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.WATER_BUCKET));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.WATER_BUCKET).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingPowderSnowBucketPlacesPowderSnow(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.POWDER_SNOW_BUCKET));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(helper, OUTPUT_POSITION)
                    .is(Blocks.POWDER_SNOW);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.BUCKET);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingPowderSnowBucketWithObstructedBlockDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.POWDER_SNOW_BUCKET));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.POWDER_SNOW_BUCKET).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSalmonBucketPlacesWaterAndSpawnsSalmon(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.SALMON_BUCKET));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.fluidState(helper, OUTPUT_POSITION)
                    .is(FluidTags.WATER);
                helper.assertEntityPresent(EntityType.SALMON);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.BUCKET);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingSalmonBucketWithObstructedBlockDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.SALMON_BUCKET));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.SALMON_BUCKET).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.water")
    public void dispensingBucketPicksUpFluid(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.BUCKET));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.fluidState(helper, OUTPUT_POSITION)
                    .is(Fluids.EMPTY);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.WATER_BUCKET);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingBucketWithNothingToPickUpDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.BUCKET));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.BUCKET).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.dirt")
    public void dispensingWaterBottleConvertsBlockToMud(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        ItemStack stack = PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.POTION), Potions.WATER);
        blockEntity.insertItem(stack);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(helper, OUTPUT_POSITION)
                    .is(Blocks.MUD);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.GLASS_BOTTLE);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingWaterBottleOnInvalidBlockDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        ItemStack stack = PotionContentsUtil.setPotion(level.itematic$createStack(ItemIds.POTION), Potions.WATER);
        blockEntity.insertItem(stack);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.POTION).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.beehive")
    public void dispensingGlassBottleOnBeehiveFillsBottleWithHoney(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.GLASS_BOTTLE));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(helper, OUTPUT_POSITION)
                    .hasProperty(BlockStateProperties.LEVEL_HONEY, 0, () -> "Expected honey level to be reset");
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.HONEY_BOTTLE);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.water")
    public void dispensingGlassBottleOnWaterFillsBottleWithWater(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.GLASS_BOTTLE));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> Assert.itemStack(helper, blockEntity.getItem(0))
                .is(ItemIds.POTION)
                .hasPotion(Potions.WATER))
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingGlassBottleOnInvalidBlockDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.GLASS_BOTTLE));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.GLASS_BOTTLE).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHorseArmorOnHorseEquipsHorse(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.IRON_HORSE_ARMOR));
        Horse horse = TestUtil.createEntity(helper, EntityType.HORSE, entity -> {
            TestUtil.setEntityPos(helper, entity, OUTPUT_POSITION);
            entity.setTamed(true);
        });
        level.addFreshEntity(horse);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.itemStack(helper, horse.getBodyArmorItem())
                    .is(ItemIds.IRON_HORSE_ARMOR);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHorseArmorWithNoEntityDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.IRON_HORSE_ARMOR));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.IRON_HORSE_ARMOR).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingCarpetOnLlamaEquipsLlama(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.WHITE_CARPET));
        Llama llama = TestUtil.createEntity(helper, EntityType.LLAMA, entity -> {
            TestUtil.setEntityPos(helper, entity, OUTPUT_POSITION);
            entity.setTamed(true);
        });
        level.addFreshEntity(llama);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.itemStack(helper, llama.getBodyArmorItem())
                    .is(ItemIds.WHITE_CARPET);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingCarpetWithNoEntityDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.WHITE_CARPET));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.WHITE_CARPET).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingChestOnMuleEquipsMuleWithChest(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.CHEST));
        Mule mule = TestUtil.createEntity(helper, EntityType.MULE, entity -> {
            TestUtil.setEntityPos(helper, entity, OUTPUT_POSITION);
            entity.setTamed(true);
        });
        level.addFreshEntity(mule);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.isTrue(
                    helper,
                    mule.hasChest(),
                    () -> "Expected Mule to have a chest"
                );
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingChestOnLlamaEquipsLlamaWithChest(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.CHEST));
        Llama llama = TestUtil.createEntity(helper, EntityType.LLAMA, entity -> {
            TestUtil.setEntityPos(helper, entity, OUTPUT_POSITION);
            entity.setTamed(true);
        });
        level.addFreshEntity(llama);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.isTrue(
                    helper,
                    llama.hasChest(),
                    () -> "Expected Llama to have a chest"
                );
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingChestWithNoEntityDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.CHEST));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.CHEST).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingShulkerBoxWithBlockBelowOutputPlacesShulkerBoxFacingUp(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.SHULKER_BOX));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(helper, OUTPUT_POSITION)
                    .hasProperty(DirectionalBlock.FACING, Direction.UP);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.gap_below_output")
    public void dispensingShulkerBoxWithoutBlockBelowOutputPlacesShulkerBoxWithDispenserDirection(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.SHULKER_BOX));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Direction dispenserDirection = helper.getBlockState(DISPENSER_POSITION).getValue(DirectionalBlock.FACING);
                Assert.blockState(helper, OUTPUT_POSITION)
                    .hasProperty(DirectionalBlock.FACING, dispenserDirection);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingShulkerBoxWithObstructedBlockKeepsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.SHULKER_BOX));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityNotPresent(level.itematic$getItem(ItemIds.SHULKER_BOX).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.SHULKER_BOX);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingTntSpawnsTnt(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.TNT));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertEntityPresent(EntityType.TNT);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingCarvedPumpkinEquipsEntity(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(helper, player, OUTPUT_POSITION);
        level.addFreshEntity(player);
        ItemStack stack = level.itematic$createStack(ItemIds.CARVED_PUMPKIN);
        blockEntity.insertItem(stack);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.itemStack(helper, player.getItemBySlot(EquipmentSlot.HEAD))
                    .is(ItemIds.CARVED_PUMPKIN);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.iron_golem_structure")
    public void dispensingCarvedPumpkinPlacesCarvedPumpkinOnIronGolemStructure(GameTestHelper helper) {
        BlockPos offset = new BlockPos(0, 2, 0);
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION.offset(offset), BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.CARVED_PUMPKIN));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION.offset(offset)))
            .thenExecuteAfter(4, () -> {
                helper.assertEntityPresent(EntityType.IRON_GOLEM);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingCarvedPumpkinWithNoValidTargetKeepsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.CARVED_PUMPKIN));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityNotPresent(level.itematic$getItem(ItemIds.CARVED_PUMPKIN).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.CARVED_PUMPKIN);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.respawn_anchor")
    public void dispensingGlowstoneOnRespawnAnchorChargesRespawnAnchor(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.GLOWSTONE));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(helper, OUTPUT_POSITION)
                    .hasProperty(RespawnAnchorBlock.CHARGE, 1);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.respawn_anchor.full")
    public void dispensingGlowstoneOnFullRespawnAnchorKeepsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.GLOWSTONE));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityNotPresent(level.itematic$getItem(ItemIds.GLOWSTONE).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.GLOWSTONE);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingGlowstoneOnInvalidBlockDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.GLOWSTONE));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.GLOWSTONE).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingShearsOnSheepShearsSheep(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.SHEARS));
        Sheep sheep = helper.spawn(EntityType.SHEEP, OUTPUT_POSITION);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.isTrue(
                    helper,
                    sheep.isSheared(),
                    () -> "Expected Sheep to be sheared"
                );
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.WHITE_WOOL).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.beehive")
    public void dispensingShearsOnBeehiveWithHoneyShearsBeehive(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.SHEARS));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(helper, OUTPUT_POSITION)
                    .hasProperty(BeehiveBlock.HONEY_LEVEL, 0);
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.HONEYCOMB).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingShearsWithNoValidTargetKeepsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.SHEARS));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityNotPresent(level.itematic$getItem(ItemIds.SHEARS).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.SHEARS)
                    .isNotDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSaddleOnPigEquipsPig(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.SADDLE));
        Pig pig = helper.spawn(EntityType.PIG, OUTPUT_POSITION);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.livingEntity(helper, pig)
                    .hasEquippedStack(EquipmentSlot.SADDLE, stack -> stack.is(ItemIds.SADDLE));
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSaddleOnHorseEquipsHorse(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.SADDLE));
        Horse horse = TestUtil.createEntity(helper, EntityType.HORSE, entity -> {
            TestUtil.setEntityPos(helper, entity, OUTPUT_POSITION);
            entity.setTamed(true);
        });
        level.addFreshEntity(horse);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.livingEntity(helper, horse)
                    .hasEquippedStack(EquipmentSlot.SADDLE, stack -> stack.is(ItemIds.SADDLE));
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSaddleWithNoEntityDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.SADDLE));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.SADDLE).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingFlintAndSteelPlacesFire(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.FLINT_AND_STEEL));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(helper, OUTPUT_POSITION)
                    .is(Blocks.FIRE);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.gap_below_output")
    public void dispensingFlintAndSteelOnInvalidBlockKeepsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.FLINT_AND_STEEL));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityNotPresent(level.itematic$getItem(ItemIds.FLINT_AND_STEEL).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.FLINT_AND_STEEL)
                    .isNotDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingBrushDropsArmadilloScute(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.BRUSH));
        helper.spawn(EntityType.ARMADILLO, OUTPUT_POSITION);
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.ARMADILLO_SCUTE).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingBrushWithNoEntityKeepsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.BRUSH));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityNotPresent(level.itematic$getItem(ItemIds.BRUSH).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .is(ItemIds.BRUSH)
                    .isNotDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.copper_block")
    public void dispensingHoneycombWaxesBlock(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(helper.getLevel().itematic$createStack(ItemIds.HONEYCOMB));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(helper, OUTPUT_POSITION)
                    .is(Blocks.WAXED_COPPER_BLOCK);
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHoneycombOnInvalidBlockDropsItem(GameTestHelper helper) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(helper, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel level = helper.getLevel();
        blockEntity.insertItem(level.itematic$createStack(ItemIds.HONEYCOMB));
        helper.startSequence()
            .thenExecute(() -> helper.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                helper.assertItemEntityPresent(level.itematic$getItem(ItemIds.HONEYCOMB).value());
                Assert.itemStack(helper, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }
}
