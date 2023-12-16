package net.errorcraft.itematic.world.action.sequence.handler;

import com.mojang.serialization.Codec;

public record SequenceHandlerType<T extends SequenceHandler>(Codec<T> codec) {
    public SequenceHandlerType(Codec<T> codec) {
        this.codec = codec.fieldOf("entries").codec();
    }
}
