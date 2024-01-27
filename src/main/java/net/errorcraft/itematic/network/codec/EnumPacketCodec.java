package net.errorcraft.itematic.network.codec;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

public class EnumPacketCodec<B extends PacketByteBuf, V extends Enum<V>> implements PacketCodec<B, V> {
    private final Class<V> clazz;

    private EnumPacketCodec(Class<V> clazz) {
        this.clazz = clazz;
    }

    @Override
    public V decode(B buf) {
        return buf.readEnumConstant(this.clazz);
    }

    @Override
    public void encode(B buf, V value) {
        buf.writeEnumConstant(value);
    }

    public static <T extends Enum<T>> EnumPacketCodec<PacketByteBuf, T> of(Class<T> clazz) {
        return new EnumPacketCodec<>(clazz);
    }
}
