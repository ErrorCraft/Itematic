package net.errorcraft.itematic.component.type;

import net.errorcraft.itematic.network.codec.EnumPacketCodec;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record ChargeablePullProgressComponent(State state) {
    public static final PacketCodec<RegistryByteBuf, ChargeablePullProgressComponent> PACKET_CODEC = PacketCodec.tuple(
        State.PACKET_CODEC, ChargeablePullProgressComponent::state,
        ChargeablePullProgressComponent::new
    );

    public enum State {
        STARTED,
        LOADED;

        public static final PacketCodec<PacketByteBuf, State> PACKET_CODEC = EnumPacketCodec.of(State.class);
    }
}
