package net.errorcraft.itematic.util;

import net.errorcraft.itematic.network.codec.EnumPacketCodec;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.DyeColor;

public class DyeColorUtil {
    public static final PacketCodec<PacketByteBuf, DyeColor> PACKET_CODEC = EnumPacketCodec.of(DyeColor.class);

    private DyeColorUtil() {}
}
