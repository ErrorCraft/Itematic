package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.RecordItemComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(JukeboxBlock.class)
public class JukeboxBlockExtender {
    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return world.getBlockEntity(pos, BlockEntityType.JUKEBOX)
            .map(JukeboxBlockEntity::getStack)
            .flatMap(stack -> stack.itematic$getComponent(ItemComponentTypes.RECORD))
            .map(RecordItemComponent::outputSignal)
            .orElse(0);
    }
}
