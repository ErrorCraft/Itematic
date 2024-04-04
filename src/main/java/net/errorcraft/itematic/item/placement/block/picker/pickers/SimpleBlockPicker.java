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
import org.jetbrains.annotations.Nullable;

public record SimpleBlockPicker(RegistryEntry<Block> block) implements BlockPicker<SimpleBlockPicker> {
    public static final MapCodec<SimpleBlockPicker> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.BLOCK).fieldOf("block").forGetter(SimpleBlockPicker::block)
    ).apply(instance, SimpleBlockPicker::new));

    @Override
    public BlockPickerType<SimpleBlockPicker> type() {
        return BlockPickerTypes.SIMPLE;
    }

    @Override
    public RegistryEntry<Block> defaultBlock() {
        return this.block;
    }

    @Override
    public @Nullable BlockState placementState(ItemPlacementContext context) {
        return this.block.value().getPlacementState(context);
    }
}
