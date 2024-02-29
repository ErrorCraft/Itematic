package net.errorcraft.itematic.mixin.block;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallHangingSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WallHangingSignBlock.class)
public abstract class WallHangingSignBlockExtender extends AbstractSignBlock {
    @Shadow
    public abstract boolean canAttachAt(BlockState state, WorldView world, BlockPos pos);

    protected WallHangingSignBlockExtender(WoodType type, Settings settings) {
        super(type, settings);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return this.canAttachAt(state, world, pos);
    }
}
