package net.errorcraft.itematic.gametest.item;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.TestUtil;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.GameType;

public class ItemFrameTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 0, 1);
    private static final BlockPos ITEM_FRAME_POSITION = BLOCK_POSITION.offset(0, 1, 0);

    @GameTest(structure = "itematic:item.item_frame.platform")
    public void usingItemFrameOnGroundPlacesItemFrame(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, world.itematic$createStack(ItemIds.ITEM_FRAME));
        world.addFreshEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.UP);
        context.succeedIf(() -> Assert.entityType(context, EntityType.ITEM_FRAME)
            .existsAt(ITEM_FRAME_POSITION));
    }

    @GameTest(structure = "itematic:item.item_frame.platform")
    public void usingItemFrameWithEntityDataPlacesItemFrameWithSpecifiedData(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemFrame = world.itematic$createStack(ItemIds.ITEM_FRAME);
        CompoundTag entityData = new CompoundTag();
        entityData.put(
            "Item",
            ItemStack.CODEC.encodeStart(
                context.getLevel().registryAccess().createSerializationContext(NbtOps.INSTANCE),
                world.itematic$createStack(ItemIds.STICK)
            ).getOrThrow()
        );
        itemFrame.set(
            DataComponents.ENTITY_DATA,
            TypedEntityData.of(
                EntityType.ITEM_FRAME,
                entityData
            )
        );
        player.setItemInHand(InteractionHand.MAIN_HAND, itemFrame);
        world.addFreshEntity(player);
        TestUtil.useBlock(context, BLOCK_POSITION, player, Direction.UP);
        context.succeedIf(() -> Assert.entityType(context, EntityType.ITEM_FRAME)
            .existsAt(ITEM_FRAME_POSITION, itemFrameAssert -> itemFrameAssert.test(
                ItemFrame::getItem,
                stack -> Assert.itemStack(context, stack)
                    .is(ItemIds.STICK)
            )));
    }
}
