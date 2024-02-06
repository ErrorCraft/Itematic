package net.errorcraft.itematic.world.action;

import com.mojang.serialization.Codec;

public record ActionType<T extends Action<T>>(Codec<T> codec) {
}
