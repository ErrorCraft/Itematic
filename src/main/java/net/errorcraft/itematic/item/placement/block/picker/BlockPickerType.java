package net.errorcraft.itematic.item.placement.block.picker;

import com.mojang.serialization.MapCodec;

public record BlockPickerType<T extends BlockPicker<T>>(MapCodec<T> codec) {
}
