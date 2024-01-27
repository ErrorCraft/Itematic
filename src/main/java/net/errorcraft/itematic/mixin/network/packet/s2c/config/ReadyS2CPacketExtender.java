package net.errorcraft.itematic.mixin.network.packet.s2c.config;

import net.errorcraft.itematic.access.network.ClientConfigurationNetworkHandlerAccess;
import net.minecraft.network.listener.ClientConfigurationPacketListener;
import net.minecraft.network.packet.s2c.config.ReadyS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ReadyS2CPacket.class)
public class ReadyS2CPacketExtender {
    @Redirect(
        method = "apply(Lnet/minecraft/network/listener/ClientConfigurationPacketListener;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/listener/ClientConfigurationPacketListener;onReady(Lnet/minecraft/network/packet/s2c/config/ReadyS2CPacket;)V"
        )
    )
    private void reloadResources(ClientConfigurationPacketListener instance, ReadyS2CPacket packet) {
        ((ClientConfigurationNetworkHandlerAccess) instance).itematic$onReady(packet);
    }
}
