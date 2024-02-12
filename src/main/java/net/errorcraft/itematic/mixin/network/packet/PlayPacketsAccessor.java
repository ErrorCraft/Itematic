package net.errorcraft.itematic.mixin.network.packet;

import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayPackets.class)
public interface PlayPacketsAccessor {
    @Invoker("s2c")
    static <T extends Packet<ClientPlayPacketListener>> PacketType<T> s2c(String id) {
        throw new AssertionError();
    }
}
