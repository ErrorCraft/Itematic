package net.errorcraft.itematic.access.network.protocol.game;

import net.errorcraft.itematic.network.packet.s2c.play.TwirlS2CPacket;

public interface ClientGamePacketListenerAccess {
    default void itematic$onTwirl(TwirlS2CPacket packet) {}
}
