package net.errorcraft.itematic.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WallHangingSignBlock.class)
public abstract class WallHangingSignBlockExtender extends SignBlock {
    @Shadow
    public abstract boolean canPlace(BlockState state, LevelReader world, BlockPos pos);

    protected WallHangingSignBlockExtender(WoodType type, Properties settings) {
        super(type, settings);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return this.canPlace(state, world, pos);
    }
}
