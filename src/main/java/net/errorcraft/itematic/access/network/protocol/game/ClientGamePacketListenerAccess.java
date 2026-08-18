package net.errorcraft.itematic.access.network.protocol.game;

import net.errorcraft.itematic.network.protocol.game.ClientboundTwirlPacket;

public interface ClientGamePacketListenerAccess {
    default void itematic$handleTwirl(ClientboundTwirlPacket packet) {}
}
