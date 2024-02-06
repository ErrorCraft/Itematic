package net.errorcraft.itematic.item.component;

import com.mojang.serialization.Codec;

public record ItemComponentType<T extends ItemComponent<T>>(Codec<T> codec) {
}
