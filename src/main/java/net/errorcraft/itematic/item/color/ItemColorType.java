package net.errorcraft.itematic.item.color;

import com.mojang.serialization.Codec;

public record ItemColorType<T extends ItemColor>(Codec<T> codec) {
}
