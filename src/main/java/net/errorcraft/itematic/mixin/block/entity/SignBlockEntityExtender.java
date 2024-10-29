package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.access.block.entity.BlockEntityAccess;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityExtender implements BlockEntityAccess {
    @Override
    public boolean itematic$placedFromItemStack(World world, @Nullable PlayerEntity player, BlockState state, BlockPos pos, ItemStack stack) {
        boolean successful = BlockEntityAccess.super.itematic$placedFromItemStack(world, player, state, pos, stack);
        if (!world.isClient() && !successful && player != null && state.getBlock() instanceof AbstractSignBlock signBlock) {
            signBlock.openEditScreen(player, (SignBlockEntity)(Object) this, true);
        }
        return successful;
    }
}
