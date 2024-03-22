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
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
        ItemStack stack = PotionContentsComponentUtil.setPotion(world.itematic$createStack(ItemKeys.POTION), Potions.WATER);
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

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingChestOnMuleEquipsMuleWithChest(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CHEST);
        blockEntity.addToFirstFreeSlot(stack);
        MuleEntity mule = TestUtil.createEntity(context, EntityType.MULE, entity -> {
            TestUtil.setEntityPos(context, entity, IN_FRONT_OF_DISPENSER_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(mule);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> context.assertTrue(mule.hasChest(), "Expected mule to have a chest"));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingChestOnLlamaEquipsLlamaWithChest(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CHEST);
        blockEntity.addToFirstFreeSlot(stack);
        LlamaEntity llama = TestUtil.createEntity(context, EntityType.LLAMA, entity -> {
            TestUtil.setEntityPos(context, entity, IN_FRONT_OF_DISPENSER_POSITION);
            entity.setTame(true);
        });
        world.spawnEntity(llama);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> context.assertTrue(llama.hasChest(), "Expected llama to have a chest"));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingTntSpawnsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.TNT);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> context.expectEntity(EntityType.TNT));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingCarvedPumpkinEquipsEntity(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        TestUtil.setEntityPos(context, player, IN_FRONT_OF_DISPENSER_POSITION);
        world.spawnEntity(player);
        ItemStack stack = world.itematic$createStack(ItemKeys.CARVED_PUMPKIN);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(player.getEquippedStack(EquipmentSlot.HEAD), ItemKeys.CARVED_PUMPKIN));
    }

    @GameTest(templateName = "itematic:block.dispenser.iron_golem_structure")
    public void dispensingCarvedPumpkinPlacesCarvedPumpkinOnIronGolemStructure(TestContext context) {
        BlockPos offset = new BlockPos(0, 2, 0);
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION.add(offset), BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.CARVED_PUMPKIN);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION.add(offset));
        context.addInstantFinalTask(() -> context.expectEntity(EntityType.IRON_GOLEM));
    }

    @GameTest(templateName = "itematic:block.dispenser.respawn_anchor")
    public void dispensingGlowstoneOnRespawnAnchorChargesRespawnAnchor(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.GLOWSTONE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> context.expectBlockProperty(IN_FRONT_OF_DISPENSER_POSITION, RespawnAnchorBlock.CHARGES, 1));
    }

    @GameTest(templateName = "itematic:block.dispenser.respawn_anchor.full")
    public void dispensingGlowstoneOnFullRespawnAnchorKeepsItem(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.GLOWSTONE);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> context.dontExpectItem(world.itematic$getItem(ItemKeys.GLOWSTONE).value()));
    }

    @GameTest(templateName = "itematic:block.dispenser")
    public void dispensingShearsOnSheepShearsSheep(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SHEARS);
        blockEntity.addToFirstFreeSlot(stack);
        SheepEntity sheep = context.spawnEntity(EntityType.SHEEP, IN_FRONT_OF_DISPENSER_POSITION);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> {
            context.assertTrue(sheep.isSheared(), "Expected sheep to be sheared");
            context.expectItem(world.itematic$getItem(ItemKeys.WHITE_WOOL).value());
        });
    }

    @GameTest(templateName = "itematic:block.dispenser.beehive")
    public void dispensingShearsOnBeehiveWithHoneyShearsBeehive(TestContext context) {
        DispenserBlockEntity blockEntity = TestUtil.getBlockEntity(context, DISPENSER_POSITION, BlockEntityType.DISPENSER);
        ServerWorld world = context.getWorld();
        ItemStack stack = world.itematic$createStack(ItemKeys.SHEARS);
        blockEntity.addToFirstFreeSlot(stack);
        context.pushButton(BUTTON_POSITION);
        context.addInstantFinalTask(() -> {
            context.expectBlockProperty(IN_FRONT_OF_DISPENSER_POSITION, BeehiveBlock.HONEY_LEVEL, 0);
            context.expectItem(world.itematic$getItem(ItemKeys.HONEYCOMB).value());
        });
    }
}
