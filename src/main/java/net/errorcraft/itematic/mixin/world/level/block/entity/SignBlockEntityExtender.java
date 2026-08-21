package net.errorcraft.itematic.mixin.world.level.block.entity;

import net.errorcraft.itematic.access.world.level.block.entity.BlockEntityAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityExtender implements BlockEntityAccess {
    @Override
    public boolean itematic$placedFromItemStack(Level level, @Nullable Player player, BlockState state, BlockPos pos, ItemStack stack) {
        boolean successful = BlockEntityAccess.super.itematic$placedFromItemStack(level, player, state, pos, stack);
        if (!level.isClientSide() && !successful && player != null && state.getBlock() instanceof SignBlock signBlock) {
            signBlock.openTextEdit(player, (SignBlockEntity)(Object) this, true);
        }

        return successful;
    }
}
