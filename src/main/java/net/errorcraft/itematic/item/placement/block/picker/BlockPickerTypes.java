package net.errorcraft.itematic.item.placement.block.picker;

import net.errorcraft.itematic.item.placement.block.picker.pickers.AttachedToSideBlockPicker;
import net.errorcraft.itematic.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class BlockPickerTypes {
    public static final BlockPickerType<SimpleBlockPicker> SIMPLE = register(BlockPickerTypeKeys.SIMPLE, new BlockPickerType<>(SimpleBlockPicker.CODEC));
    public static final BlockPickerType<AttachedToSideBlockPicker> ATTACHED_TO_SIDE = register(BlockPickerTypeKeys.ATTACHED_TO_SIDE, new BlockPickerType<>(AttachedToSideBlockPicker.CODEC));

    private BlockPickerTypes() {}

    public static void init() {}

    private static <T extends BlockPicker<T>> BlockPickerType<T> register(RegistryKey<BlockPickerType<?>> id, BlockPickerType<T> type) {
        return Registry.register(ItematicRegistries.BLOCK_PICKER_TYPE, id, type);
    }
}
