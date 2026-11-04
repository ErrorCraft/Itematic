package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.component.BlockPlacerComponent;
import net.errorcraft.itematic.world.item.placement.block.picker.pickers.AttachedToSideBlockPicker;
import net.errorcraft.itematic.world.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public record BlockItemBehavior(BlockPlacerComponent blockPlacer) implements ItemBehavior<BlockItemBehavior> {
    public static final Codec<BlockItemBehavior> CODEC = BlockPlacerComponent.CODEC.xmap(
        BlockItemBehavior::new,
        BlockItemBehavior::blockPlacer
    );

    public static BlockItemBehavior of(Holder<Block> block) {
        return new BlockItemBehavior(
            new BlockPlacerComponent(
                new SimpleBlockPicker(block),
                false,
                BlockPlacerComponent.Pass.DEFAULT_PASSES
            )
        );
    }

    public static BlockItemBehavior of(Holder<Block> block, BlockPlacerComponent.Pass... passes) {
        return new BlockItemBehavior(
            new BlockPlacerComponent(
                new SimpleBlockPicker(block),
                false,
                Set.of(passes)
            )
        );
    }

    public static BlockItemBehavior operator(Holder<Block> block) {
        return new BlockItemBehavior(
            new BlockPlacerComponent(
                new SimpleBlockPicker(block),
                true,
                BlockPlacerComponent.Pass.DEFAULT_PASSES
            )
        );
    }

    public static BlockItemBehavior attachedToSide(Holder<Block> attachedBlock, Holder<Block> otherBlock, Direction attachedSide) {
        return new BlockItemBehavior(
            new BlockPlacerComponent(
                new AttachedToSideBlockPicker(attachedBlock, otherBlock, attachedSide),
                false,
                BlockPlacerComponent.Pass.DEFAULT_PASSES
            )
        );
    }

    @Override
    public ItemBehaviorType<BlockItemBehavior> type() {
        return ItemBehaviorType.BLOCK;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.BLOCK_PLACER, this.blockPlacer);
        this.blockPlacer.addComponents(builder);
    }
}
