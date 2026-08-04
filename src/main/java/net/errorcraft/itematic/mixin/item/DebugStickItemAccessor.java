package net.errorcraft.itematic.mixin.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DebugStickItem.class)
public interface DebugStickItemAccessor {
    @Invoker("handleInteraction")
    boolean itematic$use(Player player, BlockState state, LevelAccessor world, BlockPos pos, boolean update, ItemStack stack);
}
