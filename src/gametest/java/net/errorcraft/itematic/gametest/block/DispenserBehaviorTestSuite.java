package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
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
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class DispenserBehaviorTestSuite {
    private static final BlockPos DISPENSER_POSITION = new BlockPos(2, 2, 3);
    private static final BlockPos BUTTON_POSITION = DISPENSER_POSITION.add(0, 1, 0);
    private static final BlockPos OUTPUT_POSITION = DISPENSER_POSITION.add(0, 0, -1);
    private static final BlockPos ABOVE_OUTPUT_POSITION = OUTPUT_POSITION.add(0, 1, 0);

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingArrowSpawnsArrow(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.ARROW);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectEntity(EntityType.ARROW);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingExperienceBottleSpawnsExperienceBottle(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.EXPERIENCE_BOTTLE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectEntity(EntityType.EXPERIENCE_BOTTLE);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingFireworkRocketSpawnsFireworkRocket(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.FIREWORK_ROCKET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectEntity(EntityType.FIREWORK_ROCKET);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingFireChargeSpawnsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.FIRE_CHARGE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectEntity(EntityType.SMALL_FIREBALL);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingPigSpawnEggSpawnsPig(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.PIG_SPAWN_EGG);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectEntity(EntityType.PIG);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingArmorStandSpawnsArmorStand(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.ARMOR_STAND);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectEntity(EntityType.ARMOR_STAND);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingSpruceBoatSpawnsSpruceBoat(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SPRUCE_BOAT);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectEntityWithData(OUTPUT_POSITION, EntityType.BOAT, BoatEntity::getVariant, BoatEntity.Type.SPRUCE);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.grass_block")
    public void dispensingBoneMealFertilizesBlock(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BONE_MEAL);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.dontExpectBlock(Blocks.AIR, ABOVE_OUTPUT_POSITION);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.bedrock")
    public void dispensingBoneMealOnInvalidBlockKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BONE_MEAL);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectBlock(Blocks.AIR, ABOVE_OUTPUT_POSITION);
            context.dontExpectItem(world.itematic$getItem(ItemKeys.BONE_MEAL).value());
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.BONE_MEAL);
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingEquipmentEquipsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, OUTPUT_POSITION);
        world.spawnEntity(player);
        ItemStack stack = world.itematic$createStack(ItemKeys.IRON_HELMET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            Assert.itemStackIsOf(player.getEquippedStack(EquipmentSlot.HEAD), ItemKeys.IRON_HELMET);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingEquipmentWithNoEntityDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.IRON_HELMET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.IRON_HELMET).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingHeadEquipsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, OUTPUT_POSITION);
        world.spawnEntity(player);
        ItemStack stack = world.itematic$createStack(ItemKeys.SKELETON_SKULL);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            Assert.itemStackIsOf(player.getEquippedStack(EquipmentSlot.HEAD), ItemKeys.SKELETON_SKULL);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingHeadWithNoEntityKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SKELETON_SKULL);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.dontExpectItem(world.itematic$getItem(ItemKeys.SKELETON_SKULL).value());
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.SKELETON_SKULL);
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingWaterBucketPlacesWater(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WATER_BUCKET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            Assert.fluidIsIn(context, FluidTags.WATER, OUTPUT_POSITION);
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.BUCKET);
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.bedrock")
    public void dispensingWaterBucketWithObstructedBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WATER_BUCKET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.WATER_BUCKET).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingPowderSnowBucketPlacesPowderSnow(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.POWDER_SNOW_BUCKET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectBlock(Blocks.POWDER_SNOW, OUTPUT_POSITION);
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.BUCKET);
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.bedrock")
    public void dispensingPowderSnowBucketWithObstructedBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.POWDER_SNOW_BUCKET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.POWDER_SNOW_BUCKET).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingSalmonBucketPlacesWaterAndSpawnsSalmon(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SALMON_BUCKET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            Assert.fluidIsIn(context, FluidTags.WATER, OUTPUT_POSITION);
            context.expectEntity(EntityType.SALMON);
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.BUCKET);
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.bedrock")
    public void dispensingSalmonBucketWithObstructedBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SALMON_BUCKET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.SALMON_BUCKET).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.water")
    public void dispensingBucketPicksUpFluid(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BUCKET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            Assert.fluidIsOf(context, Fluids.EMPTY, OUTPUT_POSITION);
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.WATER_BUCKET);
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingBucketWithNothingToPickUpDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BUCKET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.BUCKET).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.dirt")
    public void dispensingWaterBottleConvertsBlockToMud(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectBlock(Blocks.MUD, OUTPUT_POSITION);
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.GLASS_BOTTLE);
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.bedrock")
    public void dispensingWaterBottleOnInvalidBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.POTION).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.beehive")
    public void dispensingGlassBottleOnBeehiveFillsBottleWithHoney(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.GLASS_BOTTLE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.checkBlockState(
                OUTPUT_POSITION,
                state -> state.get(Properties.HONEY_LEVEL) == 0,
                () -> "Honey level was not reset"
            );
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.HONEY_BOTTLE);
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.water")
    public void dispensingGlassBottleOnWaterFillsBottleWithWater(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.GLASS_BOTTLE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.POTION);
            Assert.itemStackHasPotion(blockEntity.getStack(0), Potions.WATER);
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.bedrock")
    public void dispensingGlassBottleOnInvalidBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.GLASS_BOTTLE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.GLASS_BOTTLE).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingHorseArmorOnHorseEquipsHorse(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.IRON_HORSE_ARMOR);
        blockEntity.addToFirstFreeSlot(stack);
        HorseEntity horse = TestUtil.createEntity(context, EntityType.HORSE, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(horse);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            Assert.itemStackIsOf(horse.getBodyArmor(), ItemKeys.IRON_HORSE_ARMOR);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingHorseArmorWithNoEntityDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.IRON_HORSE_ARMOR);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.IRON_HORSE_ARMOR).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingCarpetOnLlamaEquipsLlama(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WHITE_CARPET);
        blockEntity.addToFirstFreeSlot(stack);
        LlamaEntity llama = TestUtil.createEntity(context, EntityType.LLAMA, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(llama);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            Assert.itemStackIsOf(llama.getBodyArmor(), ItemKeys.WHITE_CARPET);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingCarpetWithNoEntityDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WHITE_CARPET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.WHITE_CARPET).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingChestOnMuleEquipsMuleWithChest(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CHEST);
        blockEntity.addToFirstFreeSlot(stack);
        MuleEntity mule = TestUtil.createEntity(context, EntityType.MULE, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(mule);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.assertTrue(mule.hasChest(), "Expected mule to have a chest");
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingChestOnLlamaEquipsLlamaWithChest(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CHEST);
        blockEntity.addToFirstFreeSlot(stack);
        LlamaEntity llama = TestUtil.createEntity(context, EntityType.LLAMA, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(llama);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.assertTrue(llama.hasChest(), "Expected llama to have a chest");
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingChestWithNoEntityDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CHEST);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.CHEST).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingShulkerBoxWithBlockBelowOutputPlacesShulkerBoxFacingUp(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SHULKER_BOX);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectBlockProperty(OUTPUT_POSITION, FacingBlock.FACING, Direction.UP);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.gap_below_output")
    public void dispensingShulkerBoxWithoutBlockBelowOutputPlacesShulkerBoxWithDispenserDirection(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SHULKER_BOX);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        Direction dispenserDirection = context.getBlockState(DISPENSER_POSITION).get(FacingBlock.FACING);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectBlockProperty(OUTPUT_POSITION, FacingBlock.FACING, dispenserDirection);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.bedrock")
    public void dispensingShulkerBoxWithObstructedBlockKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SHULKER_BOX);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.dontExpectItem(world.itematic$getItem(ItemKeys.SHULKER_BOX).value());
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.SHULKER_BOX);
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingTntSpawnsTnt(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.TNT);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectEntity(EntityType.TNT);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingCarvedPumpkinEquipsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, OUTPUT_POSITION);
        world.spawnEntity(player);
        ItemStack stack = world.itematic$createStack(ItemKeys.CARVED_PUMPKIN);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            Assert.itemStackIsOf(player.getEquippedStack(EquipmentSlot.HEAD), ItemKeys.CARVED_PUMPKIN);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.iron_golem_structure")
    public void dispensingCarvedPumpkinPlacesCarvedPumpkinOnIronGolemStructure(TestContext context) {
        BlockPos offset = new BlockPos(0, 2, 0);
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION.add(offset), BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CARVED_PUMPKIN);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION.add(offset));
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectEntity(EntityType.IRON_GOLEM);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingCarvedPumpkinWithNoValidTargetKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CARVED_PUMPKIN);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.dontExpectItem(world.itematic$getItem(ItemKeys.CARVED_PUMPKIN).value());
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.CARVED_PUMPKIN);
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.respawn_anchor")
    public void dispensingGlowstoneOnRespawnAnchorChargesRespawnAnchor(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.GLOWSTONE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectBlockProperty(OUTPUT_POSITION, RespawnAnchorBlock.CHARGES, 1);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.respawn_anchor.full")
    public void dispensingGlowstoneOnFullRespawnAnchorKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.GLOWSTONE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.dontExpectItem(world.itematic$getItem(ItemKeys.GLOWSTONE).value());
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.GLOWSTONE);
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingGlowstoneOnInvalidBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.GLOWSTONE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.GLOWSTONE).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingShearsOnSheepShearsSheep(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SHEARS);
        blockEntity.addToFirstFreeSlot(stack);
        SheepEntity sheep = context.spawnEntity(EntityType.SHEEP, OUTPUT_POSITION);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.assertTrue(sheep.isSheared(), "Expected sheep to be sheared");
            context.expectItem(world.itematic$getItem(ItemKeys.WHITE_WOOL).value());
            context.assertTrue(blockEntity.getStack(0).isDamaged(), "Expected item stack to be damaged");
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.beehive")
    public void dispensingShearsOnBeehiveWithHoneyShearsBeehive(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SHEARS);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectBlockProperty(OUTPUT_POSITION, BeehiveBlock.HONEY_LEVEL, 0);
            context.expectItem(world.itematic$getItem(ItemKeys.HONEYCOMB).value());
            context.assertTrue(blockEntity.getStack(0).isDamaged(), "Expected item stack to be damaged");
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingShearsWithNoValidTargetKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SHEARS);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.dontExpectItem(world.itematic$getItem(ItemKeys.SHEARS).value());
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.SHEARS);
            context.assertFalse(blockEntity.getStack(0).isDamaged(), "Expected item stack not to be damaged");
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingSaddleOnPigEquipsPig(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SADDLE);
        blockEntity.addToFirstFreeSlot(stack);
        PigEntity pig = context.spawnEntity(EntityType.PIG, OUTPUT_POSITION);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.assertTrue(pig.isSaddled(), "Expected pig to be saddled");
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingSaddleOnHorseEquipsHorse(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SADDLE);
        blockEntity.addToFirstFreeSlot(stack);
        HorseEntity horse = TestUtil.createEntity(context, EntityType.HORSE, entity -> {
            TestUtil.setEntityPos(context, entity, OUTPUT_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(horse);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.assertTrue(horse.isSaddled(), "Expected horse to be saddled");
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingSaddleWithNoEntityDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SADDLE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.SADDLE).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingFlintAndSteelPlacesFire(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.FLINT_AND_STEEL);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectBlock(Blocks.FIRE, OUTPUT_POSITION);
            context.assertTrue(blockEntity.getStack(0).isDamaged(), "Expected item stack to be damaged");
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.gap_below_output")
    public void dispensingFlintAndSteelOnInvalidBlockKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.FLINT_AND_STEEL);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.dontExpectItem(world.itematic$getItem(ItemKeys.FLINT_AND_STEEL).value());
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.FLINT_AND_STEEL);
            context.assertFalse(blockEntity.getStack(0).isDamaged(), "Expected item stack not to be damaged");
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingBrushDropsArmadilloScute(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BRUSH);
        blockEntity.addToFirstFreeSlot(stack);
        context.spawnEntity(EntityType.ARMADILLO, OUTPUT_POSITION);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.ARMADILLO_SCUTE).value());
            context.assertTrue(blockEntity.getStack(0).isDamaged(), "Expected item stack to be damaged");
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingBrushWithNoEntityKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BRUSH);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.dontExpectItem(world.itematic$getItem(ItemKeys.BRUSH).value());
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.BRUSH);
            context.assertFalse(blockEntity.getStack(0).isDamaged(), "Expected item stack not to be damaged");
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser.copper_block")
    public void dispensingHoneycombWaxesBlock(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.HONEYCOMB);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectBlock(Blocks.WAXED_COPPER_BLOCK, OUTPUT_POSITION);
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingHoneycombOnInvalidBlockDropsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.HONEYCOMB);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.runAtTick(4, () -> context.addFinalTask(() -> {
            context.expectItem(world.itematic$getItem(ItemKeys.HONEYCOMB).value());
            Assert.itemStackIsEmpty(blockEntity.getStack(0));
        }));
    }
}
