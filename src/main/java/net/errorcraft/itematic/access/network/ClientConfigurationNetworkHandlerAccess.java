package net.errorcraft.itematic.access.network;

import net.minecraft.network.packet.s2c.config.ReadyS2CPacket;

public interface ClientConfigurationNetworkHandlerAccess {
    void itematic$onReady(ReadyS2CPacket packet);
}
