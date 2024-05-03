package net.errorcraft.itematic.world.action.sequence.handler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public record SequenceHandlerType<T extends SequenceHandler<T>>(MapCodec<T> codec) {
    public SequenceHandlerType(Codec<T> codec) {
        this(codec.fieldOf("entries"));
    }
}
