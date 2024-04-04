package net.errorcraft.itematic.item.placement.block.picker;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class BlockPickerTypeKeys {
    public static final RegistryKey<BlockPickerType<?>> SIMPLE = of("simple");
    public static final RegistryKey<BlockPickerType<?>> ATTACHED_TO_SIDE = of("attached_to_side");

    private BlockPickerTypeKeys() {}

    private static RegistryKey<BlockPickerType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.BLOCK_PICKER_TYPE, new Identifier(id));
    }
}
