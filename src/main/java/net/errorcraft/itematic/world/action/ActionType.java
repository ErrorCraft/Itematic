package net.errorcraft.itematic.world.action;

import com.mojang.serialization.MapCodec;

public record ActionType<T extends Action<T>>(MapCodec<T> codec) {
}
