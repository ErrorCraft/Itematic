package net.errorcraft.itematic.network.packet;

import net.errorcraft.itematic.mixin.network.packet.PlayPacketsAccessor;
import net.errorcraft.itematic.network.packet.s2c.play.TwirlS2CPacket;
import net.minecraft.network.packet.PacketType;

public class ItematicPlayPackets {
    public static final PacketType<TwirlS2CPacket> TWIRL = PlayPacketsAccessor.s2c("twirl");

    private ItematicPlayPackets() {}
}
