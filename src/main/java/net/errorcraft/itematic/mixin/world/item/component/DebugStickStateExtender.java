package net.errorcraft.itematic.mixin.world.item.component;

import net.errorcraft.itematic.access.world.item.component.DebugStickStateAccess;
import net.errorcraft.itematic.mixin.world.item.DebugStickItemAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DebugStickState;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DebugStickState.class)
public class DebugStickStateExtender implements DebugStickStateAccess {
    @Unique
    private static final DebugStickItemAccessor DUMMY = (DebugStickItemAccessor) new DebugStickItem(new Item.Properties());

    @Override
    public void itematic$cycleProperty(LivingEntity user, BlockState state, LevelAccessor level, BlockPos pos, ItemStack stack) {
        if (user instanceof ServerPlayer playerUser) {
            DUMMY.itematic$handleInteraction(playerUser, state, level, pos, false, stack);
        }
    }

    @Override
    public InteractionResult itematic$cycleSelectedPropertyValue(UseOnContext context) {
        if (!(context.getPlayer() instanceof ServerPlayer player)) {
            return InteractionResult.SUCCESS;
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!DUMMY.itematic$handleInteraction(player, level.getBlockState(pos), level, pos, true, context.getItemInHand())) {
            return InteractionResult.FAIL;
        }

        return InteractionResult.SUCCESS;
    }
}
