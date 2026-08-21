package net.errorcraft.itematic.network.protocol.game;

import net.errorcraft.itematic.mixin.network.protocol.game.GamePacketTypesAccessor;
import net.minecraft.network.protocol.PacketType;

public class ItematicGamePacketTypes {
    public static final PacketType<ClientboundTwirlPacket> TWIRL = GamePacketTypesAccessor.createClientbound("twirl");

    private ItematicGamePacketTypes() {}
}
