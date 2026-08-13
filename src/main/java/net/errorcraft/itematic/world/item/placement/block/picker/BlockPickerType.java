package net.errorcraft.itematic.world.item.placement.block.picker;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.item.placement.block.picker.pickers.AttachedToSideBlockPicker;
import net.errorcraft.itematic.world.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.minecraft.core.Registry;

public record BlockPickerType<T extends BlockPicker<T>>(MapCodec<T> codec) {
    public static final BlockPickerType<SimpleBlockPicker> SIMPLE = register(
        "simple",
        new BlockPickerType<>(SimpleBlockPicker.CODEC)
    );
    public static final BlockPickerType<AttachedToSideBlockPicker> ATTACHED_TO_SIDE = register(
        "attached_to_side",
        new BlockPickerType<>(AttachedToSideBlockPicker.CODEC)
    );

    public static void init() {}

    private static <T extends BlockPicker<T>> BlockPickerType<T> register(String id, BlockPickerType<T> type) {
        return Registry.register(ItematicRegistries.BLOCK_PICKER_TYPE, id, type);
    }
}
