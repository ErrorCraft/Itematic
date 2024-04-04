package net.errorcraft.itematic.item.placement.block.picker.pickers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.item.placement.block.picker.BlockPickerType;
import net.errorcraft.itematic.item.placement.block.picker.BlockPickerTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public record AttachedToSideBlockPicker(RegistryEntry<Block> attachedBlock, RegistryEntry<Block> otherBlock, Direction attachedSide) implements BlockPicker<AttachedToSideBlockPicker> {
    public static final MapCodec<AttachedToSideBlockPicker> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.BLOCK).fieldOf("attached_block").forGetter(AttachedToSideBlockPicker::attachedBlock),
        RegistryFixedCodec.of(RegistryKeys.BLOCK).fieldOf("other_block").forGetter(AttachedToSideBlockPicker::otherBlock),
        Direction.CODEC.fieldOf("attached_side").forGetter(AttachedToSideBlockPicker::attachedSide)
    ).apply(instance, AttachedToSideBlockPicker::new));

    @Override
    public BlockPickerType<AttachedToSideBlockPicker> type() {
        return BlockPickerTypes.ATTACHED_TO_SIDE;
    }

    @Override
    public RegistryEntry<Block> defaultBlock() {
        return this.attachedBlock;
    }

    @Override
    public @Nullable BlockState placementState(ItemPlacementContext context) {
        BlockState state = this.otherBlock.value().getPlacementState(context);
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        for (Direction direction : context.getPlacementDirections()) {
            if (direction == this.attachedSide.getOpposite()) {
                continue;
            }
            BlockState actualState = direction == this.attachedSide ? this.attachedBlock.value().getPlacementState(context) : state;
            if (actualState != null && actualState.canPlaceAt(world, pos)) {
                return actualState;
            }
        }
        return null;
    }
}
