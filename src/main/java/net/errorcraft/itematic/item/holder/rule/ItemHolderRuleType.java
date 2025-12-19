package net.errorcraft.itematic.item.holder.rule;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record ItemHolderRuleType<T extends ItemHolderRule>(MapCodec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
}
