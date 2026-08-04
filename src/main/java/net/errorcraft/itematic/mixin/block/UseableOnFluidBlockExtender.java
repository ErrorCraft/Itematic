package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractBlockAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.FrogspawnBlock;
import net.minecraft.world.level.block.WaterlilyBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({
    WaterlilyBlock.class,
    FrogspawnBlock.class
})
public class UseableOnFluidBlockExtender implements AbstractBlockAccess {
    @Override
    public BlockPlaceContext itematic$placementContext(BlockPlaceContext context) {
        return context.itematic$offset(0, 1, 0);
    }
}
