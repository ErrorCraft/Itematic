package net.errorcraft.itematic.item.use.provider;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record IntegerProviderType<T extends IntegerProvider>(MapCodec<T> codec, PacketCodec<ByteBuf, T> packetCodec) {
}
