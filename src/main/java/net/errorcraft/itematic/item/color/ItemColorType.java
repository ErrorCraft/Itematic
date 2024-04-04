package net.errorcraft.itematic.item.color;

import com.mojang.serialization.MapCodec;

public record ItemColorType<T extends ItemColor>(MapCodec<T> codec) {
}
