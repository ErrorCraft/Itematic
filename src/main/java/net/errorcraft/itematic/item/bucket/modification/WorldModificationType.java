package net.errorcraft.itematic.item.bucket.modification;

import com.mojang.serialization.MapCodec;

public record WorldModificationType<T extends WorldModification>(MapCodec<T> codec) {
}
