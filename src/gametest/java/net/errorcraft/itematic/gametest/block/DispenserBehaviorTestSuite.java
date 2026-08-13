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
    public void dispensingArrowSpawnsArrow(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.ARROW));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertEntityPresent(EntityType.ARROW);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingExperienceBottleSpawnsExperienceBottle(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.EXPERIENCE_BOTTLE));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertEntityPresent(EntityType.EXPERIENCE_BOTTLE);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingFireworkRocketSpawnsFireworkRocket(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.FIREWORK_ROCKET));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertEntityPresent(EntityType.FIREWORK_ROCKET);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingFireChargeSpawnsEntity(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.FIRE_CHARGE));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertEntityPresent(EntityType.SMALL_FIREBALL);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingPigSpawnEggSpawnsPig(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.PIG_SPAWN_EGG));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertEntityPresent(EntityType.PIG);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingArmorStandSpawnsArmorStand(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.ARMOR_STAND));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertEntityPresent(EntityType.ARMOR_STAND);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSpruceBoatSpawnsSpruceBoat(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.SPRUCE_BOAT));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertEntityPresent(EntityType.SPRUCE_BOAT, OUTPUT_POSITION);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.grass_block")
    public void dispensingBoneMealFertilizesBlock(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.BONE_MEAL));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(context, ABOVE_OUTPUT_POSITION)
                    .isNot(Blocks.AIR);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingBoneMealOnInvalidBlockKeepsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.BONE_MEAL));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(context, ABOVE_OUTPUT_POSITION)
                    .is(Blocks.AIR);
                context.assertItemEntityNotPresent(world.itematic$getItem(ItemIds.BONE_MEAL).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.BONE_MEAL);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingEquipmentEquipsEntity(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(context, player, OUTPUT_POSITION);
        world.addFreshEntity(player);
        ItemStack stack = world.itematic$createStack(ItemIds.IRON_HELMET);
        blockEntity.insertItem(stack);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.itemStack(context, player.getItemBySlot(EquipmentSlot.HEAD))
                    .is(ItemIds.IRON_HELMET);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingEquipmentWithNoEntityDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.IRON_HELMET));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.IRON_HELMET).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHeadEquipsEntity(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(context, player, OUTPUT_POSITION);
        world.addFreshEntity(player);
        ItemStack stack = world.itematic$createStack(ItemIds.SKELETON_SKULL);
        blockEntity.insertItem(stack);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.itemStack(context, player.getItemBySlot(EquipmentSlot.HEAD))
                    .is(ItemIds.SKELETON_SKULL);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHeadWithNoEntityKeepsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.SKELETON_SKULL));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityNotPresent(world.itematic$getItem(ItemIds.SKELETON_SKULL).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.SKELETON_SKULL);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingWaterBucketPlacesWater(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.WATER_BUCKET));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.fluidState(context, OUTPUT_POSITION)
                    .is(FluidTags.WATER);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.BUCKET);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingWaterBucketWithObstructedBlockDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.WATER_BUCKET));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.WATER_BUCKET).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingPowderSnowBucketPlacesPowderSnow(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.POWDER_SNOW_BUCKET));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .is(Blocks.POWDER_SNOW);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.BUCKET);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingPowderSnowBucketWithObstructedBlockDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.POWDER_SNOW_BUCKET));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.POWDER_SNOW_BUCKET).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSalmonBucketPlacesWaterAndSpawnsSalmon(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.SALMON_BUCKET));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.fluidState(context, OUTPUT_POSITION)
                    .is(FluidTags.WATER);
                context.assertEntityPresent(EntityType.SALMON);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.BUCKET);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingSalmonBucketWithObstructedBlockDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.SALMON_BUCKET));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.SALMON_BUCKET).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.water")
    public void dispensingBucketPicksUpFluid(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.BUCKET));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.fluidState(context, OUTPUT_POSITION)
                    .is(Fluids.EMPTY);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.WATER_BUCKET);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingBucketWithNothingToPickUpDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.BUCKET));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.BUCKET).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.dirt")
    public void dispensingWaterBottleConvertsBlockToMud(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        ItemStack stack = PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.POTION), Potions.WATER);
        blockEntity.insertItem(stack);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .is(Blocks.MUD);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.GLASS_BOTTLE);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingWaterBottleOnInvalidBlockDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        ItemStack stack = PotionContentsUtil.setPotion(world.itematic$createStack(ItemIds.POTION), Potions.WATER);
        blockEntity.insertItem(stack);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.POTION).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.beehive")
    public void dispensingGlassBottleOnBeehiveFillsBottleWithHoney(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.GLASS_BOTTLE));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .hasProperty(BlockStateProperties.LEVEL_HONEY, 0, () -> "Expected honey level to be reset");
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.HONEY_BOTTLE);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.water")
    public void dispensingGlassBottleOnWaterFillsBottleWithWater(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.GLASS_BOTTLE));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> Assert.itemStack(context, blockEntity.getItem(0))
                .is(ItemIds.POTION)
                .hasPotion(Potions.WATER))
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingGlassBottleOnInvalidBlockDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.GLASS_BOTTLE));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.GLASS_BOTTLE).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHorseArmorOnHorseEquipsHorse(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.IRON_HORSE_ARMOR));
        Horse horse = TestUtil.createEntity(context, EntityType.HORSE, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTamed(true);
        });
        world.addFreshEntity(horse);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.itemStack(context, horse.getBodyArmorItem())
                    .is(ItemIds.IRON_HORSE_ARMOR);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHorseArmorWithNoEntityDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.IRON_HORSE_ARMOR));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.IRON_HORSE_ARMOR).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingCarpetOnLlamaEquipsLlama(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.WHITE_CARPET));
        Llama llama = TestUtil.createEntity(context, EntityType.LLAMA, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTamed(true);
        });
        world.addFreshEntity(llama);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.itemStack(context, llama.getBodyArmorItem())
                    .is(ItemIds.WHITE_CARPET);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingCarpetWithNoEntityDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.WHITE_CARPET));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.WHITE_CARPET).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingChestOnMuleEquipsMuleWithChest(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.CHEST));
        Mule mule = TestUtil.createEntity(context, EntityType.MULE, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTamed(true);
        });
        world.addFreshEntity(mule);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.isTrue(
                    context,
                    mule.hasChest(),
                    () -> "Expected Mule to have a chest"
                );
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingChestOnLlamaEquipsLlamaWithChest(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.CHEST));
        Llama llama = TestUtil.createEntity(context, EntityType.LLAMA, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTamed(true);
        });
        world.addFreshEntity(llama);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.isTrue(
                    context,
                    llama.hasChest(),
                    () -> "Expected Llama to have a chest"
                );
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingChestWithNoEntityDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.CHEST));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.CHEST).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingShulkerBoxWithBlockBelowOutputPlacesShulkerBoxFacingUp(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.SHULKER_BOX));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .hasProperty(DirectionalBlock.FACING, Direction.UP);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.gap_below_output")
    public void dispensingShulkerBoxWithoutBlockBelowOutputPlacesShulkerBoxWithDispenserDirection(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.SHULKER_BOX));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Direction dispenserDirection = context.getBlockState(DISPENSER_POSITION).getValue(DirectionalBlock.FACING);
                Assert.blockState(context, OUTPUT_POSITION)
                    .hasProperty(DirectionalBlock.FACING, dispenserDirection);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.bedrock")
    public void dispensingShulkerBoxWithObstructedBlockKeepsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.SHULKER_BOX));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityNotPresent(world.itematic$getItem(ItemIds.SHULKER_BOX).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.SHULKER_BOX);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingTntSpawnsTnt(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.TNT));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertEntityPresent(EntityType.TNT);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingCarvedPumpkinEquipsEntity(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        TestUtil.setEntityPos(context, player, OUTPUT_POSITION);
        world.addFreshEntity(player);
        ItemStack stack = world.itematic$createStack(ItemIds.CARVED_PUMPKIN);
        blockEntity.insertItem(stack);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.itemStack(context, player.getItemBySlot(EquipmentSlot.HEAD))
                    .is(ItemIds.CARVED_PUMPKIN);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.iron_golem_structure")
    public void dispensingCarvedPumpkinPlacesCarvedPumpkinOnIronGolemStructure(GameTestHelper context) {
        BlockPos offset = new BlockPos(0, 2, 0);
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION.offset(offset), BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.CARVED_PUMPKIN));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION.offset(offset)))
            .thenExecuteAfter(4, () -> {
                context.assertEntityPresent(EntityType.IRON_GOLEM);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingCarvedPumpkinWithNoValidTargetKeepsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.CARVED_PUMPKIN));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityNotPresent(world.itematic$getItem(ItemIds.CARVED_PUMPKIN).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.CARVED_PUMPKIN);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.respawn_anchor")
    public void dispensingGlowstoneOnRespawnAnchorChargesRespawnAnchor(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.GLOWSTONE));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .hasProperty(RespawnAnchorBlock.CHARGE, 1);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.respawn_anchor.full")
    public void dispensingGlowstoneOnFullRespawnAnchorKeepsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.GLOWSTONE));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityNotPresent(world.itematic$getItem(ItemIds.GLOWSTONE).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.GLOWSTONE);
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingGlowstoneOnInvalidBlockDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.GLOWSTONE));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.GLOWSTONE).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingShearsOnSheepShearsSheep(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.SHEARS));
        Sheep sheep = context.spawn(EntityType.SHEEP, OUTPUT_POSITION);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.isTrue(
                    context,
                    sheep.isSheared(),
                    () -> "Expected Sheep to be sheared"
                );
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.WHITE_WOOL).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.beehive")
    public void dispensingShearsOnBeehiveWithHoneyShearsBeehive(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.SHEARS));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .hasProperty(BeehiveBlock.HONEY_LEVEL, 0);
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.HONEYCOMB).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingShearsWithNoValidTargetKeepsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.SHEARS));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityNotPresent(world.itematic$getItem(ItemIds.SHEARS).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.SHEARS)
                    .isNotDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSaddleOnPigEquipsPig(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.SADDLE));
        Pig pig = context.spawn(EntityType.PIG, OUTPUT_POSITION);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.livingEntity(context, pig)
                    .hasEquippedStack(EquipmentSlot.SADDLE, stack -> stack.is(ItemIds.SADDLE));
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSaddleOnHorseEquipsHorse(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.SADDLE));
        Horse horse = TestUtil.createEntity(context, EntityType.HORSE, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTamed(true);
        });
        world.addFreshEntity(horse);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.livingEntity(context, horse)
                    .hasEquippedStack(EquipmentSlot.SADDLE, stack -> stack.is(ItemIds.SADDLE));
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingSaddleWithNoEntityDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.SADDLE));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.SADDLE).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingFlintAndSteelPlacesFire(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.FLINT_AND_STEEL));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .is(Blocks.FIRE);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.gap_below_output")
    public void dispensingFlintAndSteelOnInvalidBlockKeepsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.FLINT_AND_STEEL));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityNotPresent(world.itematic$getItem(ItemIds.FLINT_AND_STEEL).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.FLINT_AND_STEEL)
                    .isNotDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingBrushDropsArmadilloScute(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.BRUSH));
        context.spawn(EntityType.ARMADILLO, OUTPUT_POSITION);
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.ARMADILLO_SCUTE).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingBrushWithNoEntityKeepsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.BRUSH));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityNotPresent(world.itematic$getItem(ItemIds.BRUSH).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .is(ItemIds.BRUSH)
                    .isNotDamaged();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser.copper_block")
    public void dispensingHoneycombWaxesBlock(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        blockEntity.insertItem(context.getLevel().itematic$createStack(ItemIds.HONEYCOMB));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                Assert.blockState(context, OUTPUT_POSITION)
                    .is(Blocks.WAXED_COPPER_BLOCK);
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }

    @GameTest(structure = "itematic:block.dispenser")
    public void dispensingHoneycombOnInvalidBlockDropsItem(GameTestHelper context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerLevel world = context.getLevel();
        blockEntity.insertItem(world.itematic$createStack(ItemIds.HONEYCOMB));
        context.startSequence()
            .thenExecute(() -> context.pressButton(BUTTON_POSITION))
            .thenExecuteAfter(4, () -> {
                context.assertItemEntityPresent(world.itematic$getItem(ItemIds.HONEYCOMB).value());
                Assert.itemStack(context, blockEntity.getItem(0))
                    .isEmpty();
            })
            .thenSucceed();
    }
}
