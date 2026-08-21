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

public class GlowItemFrameTestSuite {
    private static final BlockPos BLOCK_POSITION = new BlockPos(1, 0, 1);
    private static final BlockPos GLOW_ITEM_FRAME_POSITION = BLOCK_POSITION.offset(0, 1, 0);

    @GameTest(structure = "itematic:item.glow_item_frame.platform")
    public void usingGlowItemFrameOnGroundPlacesGlowItemFrame(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(
            InteractionHand.MAIN_HAND,
            level.itematic$createStack(ItemIds.GLOW_ITEM_FRAME)
        );
        level.addFreshEntity(player);
        TestUtil.interactWithBlock(helper, BLOCK_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.GLOW_ITEM_FRAME)
            .existsAt(GLOW_ITEM_FRAME_POSITION));
    }

    @GameTest(structure = "itematic:item.glow_item_frame.platform")
    public void usingGlowItemFrameWithEntityDataPlacesGlowItemFrameWithSpecifiedData(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack glowItemFrame = level.itematic$createStack(ItemIds.GLOW_ITEM_FRAME);
        CompoundTag entityData = new CompoundTag();
        entityData.put(
            "Item",
            ItemStack.CODEC.encodeStart(
                helper.getLevel().registryAccess().createSerializationContext(NbtOps.INSTANCE),
                level.itematic$createStack(ItemIds.STICK)
            ).getOrThrow()
        );
        glowItemFrame.set(
            DataComponents.ENTITY_DATA,
            TypedEntityData.of(
                EntityType.GLOW_ITEM_FRAME,
                entityData
            )
        );
        player.setItemInHand(InteractionHand.MAIN_HAND, glowItemFrame);
        level.addFreshEntity(player);
        TestUtil.interactWithBlock(helper, BLOCK_POSITION, player, Direction.UP);
        helper.succeedIf(() -> Assert.entityType(helper, EntityType.GLOW_ITEM_FRAME)
            .existsAt(GLOW_ITEM_FRAME_POSITION, glowItemFrameAssert -> glowItemFrameAssert.test(
                ItemFrame::getItem,
                stack -> Assert.itemStack(helper, stack)
                    .is(ItemIds.STICK)
            )));
    }
}
