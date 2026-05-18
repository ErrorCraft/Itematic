package net.errorcraft.itematic.world.modification;

import com.mojang.serialization.MapCodec;

public record WorldModificationType<T extends WorldModification>(MapCodec<T> codec) {
}
