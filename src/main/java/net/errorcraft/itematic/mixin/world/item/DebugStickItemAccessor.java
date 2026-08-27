package net.errorcraft.itematic.mixin.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DebugStickItem.class)
public interface DebugStickItemAccessor {
    @Invoker("handleInteraction")
    boolean itematic$handleInteraction(ServerPlayer player, BlockState state, LevelAccessor level, BlockPos pos, boolean cycle, ItemStack itemStackInHand);
}
