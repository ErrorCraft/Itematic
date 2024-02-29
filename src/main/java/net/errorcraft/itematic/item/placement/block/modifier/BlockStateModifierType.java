package net.errorcraft.itematic.item.placement.block.modifier;

import com.mojang.serialization.Codec;

public record BlockStateModifierType<T extends BlockStateModifier<T>>(Codec<T> codec) {
}
