package net.errorcraft.itematic.access.network.listener;

import net.errorcraft.itematic.network.packet.s2c.play.TwirlS2CPacket;

public interface ClientPlayPacketListenerAccess {
    default void itematic$onTwirl(TwirlS2CPacket packet) {}
}
