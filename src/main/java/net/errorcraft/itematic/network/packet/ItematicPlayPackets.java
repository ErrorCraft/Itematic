package net.errorcraft.itematic.network.packet;

import net.errorcraft.itematic.mixin.network.protocol.game.GamePacketTypesAccessor;
import net.errorcraft.itematic.network.packet.s2c.play.TwirlS2CPacket;
import net.minecraft.network.protocol.PacketType;

public class ItematicPlayPackets {
    public static final PacketType<TwirlS2CPacket> TWIRL = GamePacketTypesAccessor.createClientbound("twirl");

    private ItematicPlayPackets() {}
}
