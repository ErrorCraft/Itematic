package net.errorcraft.itematic.item.placement.block.picker;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class BlockPickerTypeKeys {
    public static final ResourceKey<BlockPickerType<?>> SIMPLE = of("simple");
    public static final ResourceKey<BlockPickerType<?>> ATTACHED_TO_SIDE = of("attached_to_side");

    private BlockPickerTypeKeys() {}

    private static ResourceKey<BlockPickerType<?>> of(String id) {
        return ResourceKey.create(ItematicRegistryKeys.BLOCK_PICKER_TYPE, Identifier.withDefaultNamespace(id));
    }
}
