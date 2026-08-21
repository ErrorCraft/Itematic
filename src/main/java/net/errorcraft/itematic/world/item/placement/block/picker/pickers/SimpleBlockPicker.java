package net.errorcraft.itematic.world.item.placement.block.picker.pickers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.world.item.placement.block.picker.BlockPickerType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public record SimpleBlockPicker(Holder<Block> block) implements BlockPicker<SimpleBlockPicker> {
    public static final MapCodec<SimpleBlockPicker> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryFixedCodec.create(Registries.BLOCK).fieldOf("block").forGetter(SimpleBlockPicker::block)
    ).apply(instance, SimpleBlockPicker::new));

    @Override
    public BlockPickerType<SimpleBlockPicker> type() {
        return BlockPickerType.SIMPLE;
    }

    @Override
    public Holder<Block> defaultBlock() {
        return this.block;
    }

    @Override
    public @Nullable BlockState placementState(BlockPlaceContext context) {
        return this.block.value().getStateForPlacement(context);
    }
}
