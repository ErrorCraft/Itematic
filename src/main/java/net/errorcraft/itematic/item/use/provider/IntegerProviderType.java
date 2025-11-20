package net.errorcraft.itematic.item.use.provider;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record IntegerProviderType<T extends IntegerProvider>(MapCodec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
}
