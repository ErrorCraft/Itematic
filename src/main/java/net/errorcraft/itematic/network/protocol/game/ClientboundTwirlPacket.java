package net.errorcraft.itematic.network.protocol.game;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

public record ClientboundTwirlPacket(float spinAttackStrength) implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTwirlPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, ClientboundTwirlPacket::spinAttackStrength,
        ClientboundTwirlPacket::new
    );

    @Override
    public PacketType<? extends Packet<ClientGamePacketListener>> type() {
        return ItematicGamePacketTypes.TWIRL;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.itematic$handleTwirl(this);
    }
}
