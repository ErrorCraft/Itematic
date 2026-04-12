package net.errorcraft.itematic.mixin.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DebugStickItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DebugStickItem.class)
public interface DebugStickItemAccessor {
    @Invoker("use")
    boolean itematic$use(PlayerEntity player, BlockState state, WorldAccess world, BlockPos pos, boolean update, ItemStack stack);
}
