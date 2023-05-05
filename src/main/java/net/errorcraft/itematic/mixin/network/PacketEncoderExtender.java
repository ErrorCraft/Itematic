package net.errorcraft.itematic.mixin.network;

import net.errorcraft.itematic.access.registry.DynamicRegistryManagerAccess;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketEncoder;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PacketEncoder.class)
public class PacketEncoderExtender implements DynamicRegistryManagerAccess {
    private DynamicRegistryManager registryManager;

    @ModifyVariable(
        method = "encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;Lio/netty/buffer/ByteBuf;)V",
        at = @At("STORE")
    )
    private PacketByteBuf encodeUseDynamicRegistryManager(PacketByteBuf packetByteBuf) {
        packetByteBuf.setRegistryManager(this.registryManager);
        return packetByteBuf;
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }
}
