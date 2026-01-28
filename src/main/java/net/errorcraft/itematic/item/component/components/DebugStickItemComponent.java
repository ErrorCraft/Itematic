package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.item.DebugStickItemAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DebugStickStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DebugStickItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public record DebugStickItemComponent() implements ItemComponent<DebugStickItemComponent> {
    public static final DebugStickItemComponent INSTANCE = new DebugStickItemComponent();
    public static final Codec<DebugStickItemComponent> CODEC = Codec.unit(INSTANCE);
    private static final DebugStickItemAccessor DUMMY = (DebugStickItemAccessor) new DebugStickItem(new Item.Settings());

    @Override
    public ItemComponentType<DebugStickItemComponent> type() {
        return ItemComponentTypes.DEBUG_STICK;
    }

    @Override
    public Codec<DebugStickItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        World world = context.getWorld();
        if (world.isClient()) {
            return ItemResult.PASS;
        }
        PlayerEntity player = context.getPlayer();
        if (player == null) {
            return ItemResult.PASS;
        }
        BlockPos pos = context.getBlockPos();
        if (!DUMMY.callUse(player, world.getBlockState(pos), world, pos, true, context.getStack())) {
            return ItemResult.PASS;
        }
        return ItemResult.SUCCEED;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.DEBUG_STICK_STATE, DebugStickStateComponent.DEFAULT);
    }

    public void use(PlayerEntity miner, BlockState state, WorldAccess world, BlockPos pos) {
        if (!world.isClient()) {
            DUMMY.callUse(miner, state, world, pos, false, miner.getStackInHand(Hand.MAIN_HAND));
        }
    }
}
