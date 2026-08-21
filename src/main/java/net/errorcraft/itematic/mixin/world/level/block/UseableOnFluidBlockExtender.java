package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.access.world.level.block.state.BlockBehaviourAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.FrogspawnBlock;
import net.minecraft.world.level.block.WaterlilyBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({
    WaterlilyBlock.class,
    FrogspawnBlock.class
})
public class UseableOnFluidBlockExtender implements BlockBehaviourAccess {
    @Override
    public BlockPlaceContext itematic$blockPlaceContext(BlockPlaceContext context) {
        return context.itematic$offset(0, 1, 0);
    }
}
