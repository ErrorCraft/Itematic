package net.errorcraft.itematic.item.placement.block.modifier.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.placement.block.modifier.BlockStateModifier;
import net.errorcraft.itematic.item.placement.block.modifier.BlockStateModifierType;
import net.errorcraft.itematic.item.placement.block.modifier.BlockStateModifierTypes;
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

public record AttachedToSideBlockStateModifier(RegistryEntry<Block> attachedBlock, RegistryEntry<Block> otherBlock, Direction attachedSide) implements BlockStateModifier<AttachedToSideBlockStateModifier> {
    public static final Codec<AttachedToSideBlockStateModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.BLOCK).fieldOf("attached_block").forGetter(AttachedToSideBlockStateModifier::attachedBlock),
        RegistryFixedCodec.of(RegistryKeys.BLOCK).fieldOf("other_block").forGetter(AttachedToSideBlockStateModifier::otherBlock),
        Direction.CODEC.fieldOf("attached_side").forGetter(AttachedToSideBlockStateModifier::attachedSide)
    ).apply(instance, AttachedToSideBlockStateModifier::new));

    @Override
    public BlockStateModifierType<AttachedToSideBlockStateModifier> type() {
        return BlockStateModifierTypes.ATTACHED_TO_SIDE;
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
