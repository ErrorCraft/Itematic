package net.errorcraft.itematic.mixin.network.handler;

import net.errorcraft.itematic.access.network.handler.PacketEncoderAccess;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.handler.PacketEncoder;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PacketEncoder.class)
public class PacketEncoderExtender implements PacketEncoderAccess {
    private DynamicRegistryManager registryManager;

    @ModifyVariable(
        method = "encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;Lio/netty/buffer/ByteBuf;)V",
        at = @At("STORE")
    )
    private PacketByteBuf encodeUseDynamicRegistryManager(PacketByteBuf packetByteBuf) {
        packetByteBuf.itematic$setRegistryManager(this.registryManager);
        return packetByteBuf;
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }
}
