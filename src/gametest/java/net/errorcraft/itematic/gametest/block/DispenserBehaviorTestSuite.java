package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class DispenserBehaviorTestSuite {
    private static final BlockPos DISPENSER_POSITION = new BlockPos(2, 1, 3);
    private static final BlockPos BUTTON_POSITION = DISPENSER_POSITION.add(0, 1, 0);
    private static final BlockPos OUTPUT_POSITION = DISPENSER_POSITION.add(0, 0, -1);
    private static final BlockPos ABOVE_OUTPUT_POSITION = OUTPUT_POSITION.add(0, 1, 0);

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingArrowSpawnsArrow(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.ARROW));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectEntity(EntityType.ARROW);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingExperienceBottleSpawnsExperienceBottle(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.EXPERIENCE_BOTTLE));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectEntity(EntityType.EXPERIENCE_BOTTLE);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingFireworkRocketSpawnsFireworkRocket(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.FIREWORK_ROCKET));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectEntity(EntityType.FIREWORK_ROCKET);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingFireChargeSpawnsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.FIRE_CHARGE));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectEntity(EntityType.SMALL_FIREBALL);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingPigSpawnEggSpawnsPig(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.PIG_SPAWN_EGG));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectEntity(EntityType.PIG);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingArmorStandSpawnsArmorStand(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.ARMOR_STAND));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectEntity(EntityType.ARMOR_STAND);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSpruceBoatSpawnsSpruceBoat(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.SPRUCE_BOAT));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectEntityAt(EntityType.SPRUCE_BOAT, OUTPUT_POSITION);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.grass_block")
    public void dispensingBoneMealFertilizesBlock(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.BONE_MEAL));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.blockState(context, ABOVE_OUTPUT_POSITION)
                    .isNot(Blocks.AIR);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingBoneMealOnInvalidBlockKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.BONE_MEAL));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.blockState(context, ABOVE_OUTPUT_POSITION)
                    .is(Blocks.AIR);
                context.dontExpectItem(world.itematic$getItem(ItemKeys.BONE_MEAL).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.BONE_MEAL);
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingEquipmentEquipsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, OUTPUT_POSITION);
        world.spawnEntity(player);
        ItemStack stack = world.itematic$createStack(ItemKeys.IRON_HELMET);
        blockEntity.addToFirstFreeSlot(stack);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.itemStack(context, player.getEquippedStack(EquipmentSlot.HEAD))
                    .is(ItemKeys.IRON_HELMET);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingEquipmentWithNoEntityDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.IRON_HELMET));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.IRON_HELMET).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHeadEquipsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, OUTPUT_POSITION);
        world.spawnEntity(player);
        ItemStack stack = world.itematic$createStack(ItemKeys.SKELETON_SKULL);
        blockEntity.addToFirstFreeSlot(stack);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.itemStack(context, player.getEquippedStack(EquipmentSlot.HEAD))
                    .is(ItemKeys.SKELETON_SKULL);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHeadWithNoEntityKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.SKELETON_SKULL));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.dontExpectItem(world.itematic$getItem(ItemKeys.SKELETON_SKULL).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.SKELETON_SKULL);
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingWaterBucketPlacesWater(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.WATER_BUCKET));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.fluidState(context, OUTPUT_POSITION)
                    .is(FluidTags.WATER);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.BUCKET);
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingWaterBucketWithObstructedBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.WATER_BUCKET));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.WATER_BUCKET).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingPowderSnowBucketPlacesPowderSnow(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.POWDER_SNOW_BUCKET));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .is(Blocks.POWDER_SNOW);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.BUCKET);
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingPowderSnowBucketWithObstructedBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.POWDER_SNOW_BUCKET));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.POWDER_SNOW_BUCKET).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSalmonBucketPlacesWaterAndSpawnsSalmon(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.SALMON_BUCKET));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.fluidState(context, OUTPUT_POSITION)
                    .is(FluidTags.WATER);
                context.expectEntity(EntityType.SALMON);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.BUCKET);
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingSalmonBucketWithObstructedBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.SALMON_BUCKET));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.SALMON_BUCKET).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.water")
    public void dispensingBucketPicksUpFluid(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.BUCKET));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.fluidState(context, OUTPUT_POSITION)
                    .is(Fluids.EMPTY);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.WATER_BUCKET);
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingBucketWithNothingToPickUpDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.BUCKET));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.BUCKET).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.dirt")
    public void dispensingWaterBottleConvertsBlockToMud(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER);
        blockEntity.addToFirstFreeSlot(stack);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .is(Blocks.MUD);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.GLASS_BOTTLE);
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingWaterBottleOnInvalidBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER);
        blockEntity.addToFirstFreeSlot(stack);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.POTION).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.beehive")
    public void dispensingGlassBottleOnBeehiveFillsBottleWithHoney(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.GLASS_BOTTLE));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .hasProperty(Properties.HONEY_LEVEL, 0, () -> "Expected honey level to be reset");
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.HONEY_BOTTLE);
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.water")
    public void dispensingGlassBottleOnWaterFillsBottleWithWater(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.GLASS_BOTTLE));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> Assert.itemStack(context, blockEntity.getStack(0))
                .is(ItemKeys.POTION)
                .hasPotion(Potions.WATER))
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingGlassBottleOnInvalidBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.GLASS_BOTTLE));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.GLASS_BOTTLE).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHorseArmorOnHorseEquipsHorse(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.IRON_HORSE_ARMOR));
        HorseEntity horse = TestUtil.createEntity(context, EntityType.HORSE, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(horse);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.itemStack(context, horse.getBodyArmor())
                    .is(ItemKeys.IRON_HORSE_ARMOR);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHorseArmorWithNoEntityDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.IRON_HORSE_ARMOR));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.IRON_HORSE_ARMOR).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingCarpetOnLlamaEquipsLlama(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.WHITE_CARPET));
        LlamaEntity llama = TestUtil.createEntity(context, EntityType.LLAMA, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(llama);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.itemStack(context, llama.getBodyArmor())
                    .is(ItemKeys.WHITE_CARPET);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingCarpetWithNoEntityDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.WHITE_CARPET));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.WHITE_CARPET).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingChestOnMuleEquipsMuleWithChest(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.CHEST));
        MuleEntity mule = TestUtil.createEntity(context, EntityType.MULE, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(mule);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.isTrue(
                    context,
                    mule.hasChest(),
                    () -> "Expected Mule to have a chest"
                );
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingChestOnLlamaEquipsLlamaWithChest(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.CHEST));
        LlamaEntity llama = TestUtil.createEntity(context, EntityType.LLAMA, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(llama);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.isTrue(
                    context,
                    llama.hasChest(),
                    () -> "Expected Llama to have a chest"
                );
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingChestWithNoEntityDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.CHEST));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.CHEST).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingShulkerBoxWithBlockBelowOutputPlacesShulkerBoxFacingUp(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.SHULKER_BOX));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .hasProperty(FacingBlock.FACING, Direction.UP);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.gap_below_output")
    public void dispensingShulkerBoxWithoutBlockBelowOutputPlacesShulkerBoxWithDispenserDirection(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.SHULKER_BOX));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Direction dispenserDirection = context.getBlockState(DISPENSER_POSITION).get(FacingBlock.FACING);
                Assert.blockState(context, OUTPUT_POSITION)
                    .hasProperty(FacingBlock.FACING, dispenserDirection);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingShulkerBoxWithObstructedBlockKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.SHULKER_BOX));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.dontExpectItem(world.itematic$getItem(ItemKeys.SHULKER_BOX).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.SHULKER_BOX);
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingTntSpawnsTnt(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.TNT));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectEntity(EntityType.TNT);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingCarvedPumpkinEquipsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, OUTPUT_POSITION);
        world.spawnEntity(player);
        ItemStack stack = world.itematic$createStack(ItemKeys.CARVED_PUMPKIN);
        blockEntity.addToFirstFreeSlot(stack);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.itemStack(context, player.getEquippedStack(EquipmentSlot.HEAD))
                    .is(ItemKeys.CARVED_PUMPKIN);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.iron_golem_structure")
    public void dispensingCarvedPumpkinPlacesCarvedPumpkinOnIronGolemStructure(TestContext context) {
        BlockPos offset = new BlockPos(0, 2, 0);
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION.add(offset), BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.CARVED_PUMPKIN));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION.add(offset)))
            .expectMinDurationAndRun(4, () -> {
                context.expectEntity(EntityType.IRON_GOLEM);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingCarvedPumpkinWithNoValidTargetKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.CARVED_PUMPKIN));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.dontExpectItem(world.itematic$getItem(ItemKeys.CARVED_PUMPKIN).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.CARVED_PUMPKIN);
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.respawn_anchor")
    public void dispensingGlowstoneOnRespawnAnchorChargesRespawnAnchor(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.GLOWSTONE));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .hasProperty(RespawnAnchorBlock.CHARGES, 1);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.respawn_anchor.full")
    public void dispensingGlowstoneOnFullRespawnAnchorKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.GLOWSTONE));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.dontExpectItem(world.itematic$getItem(ItemKeys.GLOWSTONE).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.GLOWSTONE);
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingGlowstoneOnInvalidBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.GLOWSTONE));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.GLOWSTONE).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingShearsOnSheepShearsSheep(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.SHEARS));
        SheepEntity sheep = context.spawnEntity(EntityType.SHEEP, OUTPUT_POSITION);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.isTrue(
                    context,
                    sheep.isSheared(),
                    () -> "Expected Sheep to be sheared"
                );
                context.expectItem(world.itematic$getItem(ItemKeys.WHITE_WOOL).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isDamaged();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.beehive")
    public void dispensingShearsOnBeehiveWithHoneyShearsBeehive(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.SHEARS));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .hasProperty(BeehiveBlock.HONEY_LEVEL, 0);
                context.expectItem(world.itematic$getItem(ItemKeys.HONEYCOMB).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isDamaged();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingShearsWithNoValidTargetKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.SHEARS));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.dontExpectItem(world.itematic$getItem(ItemKeys.SHEARS).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.SHEARS)
                    .isNotDamaged();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSaddleOnPigEquipsPig(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.SADDLE));
        PigEntity pig = context.spawnEntity(EntityType.PIG, OUTPUT_POSITION);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.livingEntity(context, pig)
                    .hasEquippedStack(EquipmentSlot.SADDLE, stack -> stack.is(ItemKeys.SADDLE));
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSaddleOnHorseEquipsHorse(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.SADDLE));
        HorseEntity horse = TestUtil.createEntity(context, EntityType.HORSE, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(horse);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.livingEntity(context, horse)
                    .hasEquippedStack(EquipmentSlot.SADDLE, stack -> stack.is(ItemKeys.SADDLE));
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSaddleWithNoEntityDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.SADDLE));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.SADDLE).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingFlintAndSteelPlacesFire(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.FLINT_AND_STEEL));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .is(Blocks.FIRE);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isDamaged();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.gap_below_output")
    public void dispensingFlintAndSteelOnInvalidBlockKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.FLINT_AND_STEEL));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.dontExpectItem(world.itematic$getItem(ItemKeys.FLINT_AND_STEEL).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.FLINT_AND_STEEL)
                    .isNotDamaged();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingBrushDropsArmadilloScute(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.BRUSH));
        context.spawnEntity(EntityType.ARMADILLO, OUTPUT_POSITION);
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.ARMADILLO_SCUTE).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isDamaged();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingBrushWithNoEntityKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.BRUSH));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.dontExpectItem(world.itematic$getItem(ItemKeys.BRUSH).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .is(ItemKeys.BRUSH)
                    .isNotDamaged();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser.copper_block")
    public void dispensingHoneycombWaxesBlock(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.addToFirstFreeSlot(context.getWorld().itematic$createStack(ItemKeys.HONEYCOMB));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .is(Blocks.WAXED_COPPER_BLOCK);
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHoneycombOnInvalidBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        blockEntity.addToFirstFreeSlot(world.itematic$createStack(ItemKeys.HONEYCOMB));
        context.createTimedTaskRunner()
            .createAndAddReported(() -> context.pushButton(BUTTON_POSITION))
            .expectMinDurationAndRun(4, () -> {
                context.expectItem(world.itematic$getItem(ItemKeys.HONEYCOMB).value());
                Assert.itemStack(context, blockEntity.getStack(0))
                    .isEmpty();
            })
            .completeIfSuccessful();
    }
}
