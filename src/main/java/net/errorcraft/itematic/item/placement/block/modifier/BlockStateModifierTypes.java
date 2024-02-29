package net.errorcraft.itematic.item.placement.block.modifier;

import net.errorcraft.itematic.item.placement.block.modifier.modifiers.AttachedToSideBlockStateModifier;
import net.errorcraft.itematic.item.placement.block.modifier.modifiers.SimpleBlockStateModifier;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class BlockStateModifierTypes {
    public static final BlockStateModifierType<SimpleBlockStateModifier> SIMPLE = register(BlockStateModifierTypeKeys.SIMPLE, new BlockStateModifierType<>(SimpleBlockStateModifier.CODEC));
    public static final BlockStateModifierType<AttachedToSideBlockStateModifier> ATTACHED_TO_SIDE = register(BlockStateModifierTypeKeys.ATTACHED_TO_SIDE, new BlockStateModifierType<>(AttachedToSideBlockStateModifier.CODEC));

    private BlockStateModifierTypes() {}

    public static void init() {}

    private static <T extends BlockStateModifier<T>> BlockStateModifierType<T> register(RegistryKey<BlockStateModifierType<?>> id, BlockStateModifierType<T> type) {
        return Registry.register(ItematicRegistries.BLOCK_STATE_MODIFIER_TYPE, id, type);
    }
}
