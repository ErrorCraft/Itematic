package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.access.block.entity.BlockEntityAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityExtender implements BlockEntityAccess {
    @Override
    public boolean itematic$placedFromItemStack(Level world, @Nullable Player player, BlockState state, BlockPos pos, ItemStack stack) {
        boolean successful = BlockEntityAccess.super.itematic$placedFromItemStack(world, player, state, pos, stack);
        if (!world.isClientSide() && !successful && player != null && state.getBlock() instanceof SignBlock signBlock) {
            signBlock.openTextEdit(player, (SignBlockEntity)(Object) this, true);
        }
        return successful;
    }
}
