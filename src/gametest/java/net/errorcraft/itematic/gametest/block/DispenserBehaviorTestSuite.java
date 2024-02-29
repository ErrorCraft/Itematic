package net.errorcraft.itematic.gametest.block;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.gametest.TestUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class DispenserBehaviorTestSuite {
    private static final BlockPos DISPENSER_POSITION = new BlockPos(2, 2, 3);
    private static final BlockPos BUTTON_POSITION = new BlockPos(2, 3, 3);
    private static final BlockPos IN_FRONT_OF_DISPENSER_POSITION = new BlockPos(2, 2, 2);

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingArrowSpawnsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.ARROW);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> context.expectEntity(EntityType.ARROW));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingFireChargeSpawnsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.FIRE_CHARGE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> context.expectEntity(EntityType.SMALL_FIREBALL));
    }

    @GameTest(templateName = "itematic:block.dispenser.grass_block")
    public void dispensingBoneMealFertilizesBlock(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.BONE_MEAL);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> context.dontExpectBlock(Blocks.AIR, 2, 3, 2));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingEquipmentEquipsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, IN_FRONT_OF_DISPENSER_POSITION);
        world.spawnEntity(player);
        ItemStack stack = world.itematic$createStack(ItemKeys.IRON_HELMET);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(player.getEquippedStack(EquipmentSlot.HEAD), ItemKeys.IRON_HELMET));
    }

    @GameTest(templateName = "itematic:block.dispenser.dirt")
    public void dispensingWaterBottleConvertsBlockToMud(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = PotionUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> {
            context.expectBlock(Blocks.MUD, IN_FRONT_OF_DISPENSER_POSITION);
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.GLASS_BOTTLE);
        });
    }

    @GameTest(templateName = "itematic:block.dispenser.beehive")
    public void dispensingGlassBottleOnBeehiveFillsBottleWithHoney(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.GLASS_BOTTLE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> {
            context.checkBlockState(
                IN_FRONT_OF_DISPENSER_POSITION,
                state -> state.get(Properties.HONEY_LEVEL) == 0,
                () -> "Honey level was not reset"
            );
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.HONEY_BOTTLE);
        });
    }

    @GameTest(templateName = "itematic:block.dispenser.water")
    public void dispensingGlassBottleOnWaterFillsBottleWithWater(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.GLASS_BOTTLE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> {
            Assert.itemStackIsOf(blockEntity.getStack(0), ItemKeys.POTION);
            Assert.itemStackHasPotion(blockEntity.getStack(0), Potions.WATER);
        });
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingHorseArmorOnHorseEquipsHorse(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.IRON_HORSE_ARMOR);
        blockEntity.addToFirstFreeSlot(stack);
        HorseEntity horse = TestUtil.createEntity(context, EntityType.HORSE, entity -> {
            TestUtil.setEntityPos(context, entity, IN_FRONT_OF_DISPENSER_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(horse);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(horse.getBodyArmor(), ItemKeys.IRON_HORSE_ARMOR));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingCarpetOnLlamaEquipsLlama(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.WHITE_CARPET);
        blockEntity.addToFirstFreeSlot(stack);
        LlamaEntity llama = TestUtil.createEntity(context, EntityType.LLAMA, entity -> {
            TestUtil.setEntityPos(context, entity, IN_FRONT_OF_DISPENSER_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(llama);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(llama.getBodyArmor(), ItemKeys.WHITE_CARPET));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingShulkerBoxWithBlockBelowOutputPlacesShulkerBoxFacingUp(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SHULKER_BOX);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> context.expectBlockProperty(IN_FRONT_OF_DISPENSER_POSITION, FacingBlock.FACING, Direction.UP));
    }

    @GameTest(templateName = "itematic:block.dispenser.gap_below_output")
    public void dispensingShulkerBoxWithoutBlockBelowOutputPlacesShulkerBoxWithDispenserDirection(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SHULKER_BOX);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        Direction dispenserDirection = context.getBlockState(DISPENSER_POSITION).get(FacingBlock.FACING);
        context.addInstantFinalTask(() -> context.expectBlockProperty(IN_FRONT_OF_DISPENSER_POSITION, FacingBlock.FACING, dispenserDirection));
    }
}
