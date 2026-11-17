package net.errorcraft.itematic.world.item.placement.block.picker;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.item.placement.block.picker.pickers.AttachedToSideBlockPicker;
import net.errorcraft.itematic.world.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record BlockPickerType<T extends BlockPicker<T>>(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
    public static final BlockPickerType<SimpleBlockPicker> SIMPLE = register(
        "simple",
        new BlockPickerType<>(SimpleBlockPicker.CODEC, SimpleBlockPicker.STREAM_CODEC)
    );
    public static final BlockPickerType<AttachedToSideBlockPicker> ATTACHED_TO_SIDE = register(
        "attached_to_side",
        new BlockPickerType<>(AttachedToSideBlockPicker.CODEC, AttachedToSideBlockPicker.STREAM_CODEC)
    );

    public static void init() {}

    private static <T extends BlockPicker<T>> BlockPickerType<T> register(String id, BlockPickerType<T> type) {
        return Registry.register(ItematicBuiltInRegistries.BLOCK_PICKER_TYPE, id, type);
    }
}
