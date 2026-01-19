package net.errorcraft.itematic.network.packet.s2c.play;

import net.errorcraft.itematic.network.packet.ItematicPlayPackets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

public record TwirlS2CPacket(float spinAttackStrength) implements Packet<ClientPlayPacketListener> {
    public static final PacketCodec<RegistryByteBuf, TwirlS2CPacket> CODEC = PacketCodec.tuple(
        PacketCodecs.FLOAT, TwirlS2CPacket::spinAttackStrength,
        TwirlS2CPacket::new
    );

    @Override
    public PacketType<? extends Packet<ClientPlayPacketListener>> getPacketId() {
        return ItematicPlayPackets.TWIRL;
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {
        listener.itematic$onTwirl(this);
    }
}
