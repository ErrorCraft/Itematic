package net.errorcraft.itematic.item.placement.block.modifier;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class BlockStateModifierTypeKeys {
    public static final RegistryKey<BlockStateModifierType<?>> SIMPLE = of("simple");
    public static final RegistryKey<BlockStateModifierType<?>> ATTACHED_TO_SIDE = of("attached_to_side");

    private BlockStateModifierTypeKeys() {}

    private static RegistryKey<BlockStateModifierType<?>> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.BLOCK_STATE_MODIFIER_TYPE, new Identifier(id));
    }
}
