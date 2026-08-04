package net.errorcraft.itematic.network.packet.s2c.play;

import net.errorcraft.itematic.network.packet.ItematicPlayPackets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

public record TwirlS2CPacket(float spinAttackStrength) implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<RegistryFriendlyByteBuf, TwirlS2CPacket> CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, TwirlS2CPacket::spinAttackStrength,
        TwirlS2CPacket::new
    );

    @Override
    public PacketType<? extends Packet<ClientGamePacketListener>> type() {
        return ItematicPlayPackets.TWIRL;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.itematic$onTwirl(this);
    }
}
