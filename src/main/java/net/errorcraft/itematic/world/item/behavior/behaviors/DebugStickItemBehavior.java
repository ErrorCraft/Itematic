package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.mixin.world.item.DebugStickItemAccessor;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DebugStickState;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class DebugStickItemBehavior implements ItemBehavior<DebugStickItemBehavior> {
    public static final DebugStickItemBehavior INSTANCE = new DebugStickItemBehavior();
    public static final Codec<DebugStickItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);
    private static final DebugStickItemAccessor DUMMY = (DebugStickItemAccessor) new DebugStickItem(new Item.Properties());

    private DebugStickItemBehavior() {}

    @Override
    public ItemBehaviorType<DebugStickItemBehavior> type() {
        return ItemBehaviorType.DEBUG_STICK;
    }

    @Override
    public ItemResult useOnBlock(UseOnContext context, ItemStackExchanger stackExchanger) {
        if (!(context.getPlayer() instanceof ServerPlayer player)) {
            return ItemResult.PASS;
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!DUMMY.itematic$handleInteraction(player, level.getBlockState(pos), level, pos, true, context.getItemInHand())) {
            return ItemResult.PASS;
        }

        return ItemResult.SUCCEED;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.DEBUG_STICK_STATE, DebugStickState.EMPTY);
    }

    public void use(LivingEntity user, BlockState state, LevelAccessor level, BlockPos pos, ItemStack stack) {
        if (user instanceof ServerPlayer playerUser) {
            DUMMY.itematic$handleInteraction(playerUser, state, level, pos, false, stack);
        }
    }
}
