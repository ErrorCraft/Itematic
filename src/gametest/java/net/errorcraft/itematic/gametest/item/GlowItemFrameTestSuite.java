package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.entity.EntityTypeKeys;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class GlowItemFrameTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 0, 1);
    private static final BlockPos GLOW_ITEM_FRAME_POSITION = BLOCK_POSITION.add(0, 1, 0);

    @GameTest(structure = "itematic:item.glow_item_frame.platform")
    public void usingGlowItemFrameOnGroundPlacesGlowItemFrame(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, world.itematic$createStack(ItemKeys.GLOW_ITEM_FRAME));
        world.spawnEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.GLOW_ITEM_FRAME)
            .existsAt(GLOW_ITEM_FRAME_POSITION));
    }

    @GameTest(structure = "itematic:item.glow_item_frame.platform")
    public void usingGlowItemFrameWithEntityDataPlacesGlowItemFrameWithSpecifiedData(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack glowItemFrame = world.itematic$createStack(ItemKeys.GLOW_ITEM_FRAME);
        NbtCompound entityData = new NbtCompound();
        entityData.putString(
            Entity.ID_KEY,
            EntityTypeKeys.GLOW_ITEM_FRAME.getValue().toString()
        );
        entityData.put(
            "Item",
            ItemStack.CODEC.encodeStart(
                context.getWorld().getRegistryManager().getOps(NbtOps.INSTANCE),
                world.itematic$createStack(ItemKeys.STICK)
            ).getOrThrow()
        );
        glowItemFrame.set(DataComponentTypes.ENTITY_DATA, NbtComponent.of(entityData));
        player.setStackInHand(Hand.MAIN_HAND, glowItemFrame);
        world.spawnEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.UP);
        context.addFinalTask(() -> Assert.entityType(context, EntityType.GLOW_ITEM_FRAME)
            .existsAt(GLOW_ITEM_FRAME_POSITION, glowItemFrameAssert -> glowItemFrameAssert.test(
                ItemFrameEntity::getHeldItemStack,
                stack -> Assert.itemStack(context, stack)
                    .is(ItemKeys.STICK)
            )));
    }
}
