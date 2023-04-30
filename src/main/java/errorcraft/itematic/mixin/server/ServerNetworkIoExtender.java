package errorcraft.itematic.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.DecoderHandler;
import net.minecraft.network.PacketEncoder;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.ServerNetworkIo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ServerNetworkIoExtender {
    @Mixin(targets = "net/minecraft/server/ServerNetworkIo$1")
    public static class ChildHandlerExtender {
        @Shadow
        @Final
        ServerNetworkIo field_14112;

        @Inject(
            method = "initChannel",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/ClientConnection;addHandlers(Lio/netty/channel/ChannelPipeline;Lnet/minecraft/network/NetworkSide;)V",
                shift = At.Shift.AFTER
            )
        )
        private void initChannelSetRegistryManager(Channel channel, CallbackInfo info, @Local ChannelPipeline channelPipeline) {
            DynamicRegistryManager.Immutable registryManager = this.field_14112.getServer().getRegistryManager();
            channelPipeline.get(DecoderHandler.class).setRegistryManager(registryManager);
            channelPipeline.get(PacketEncoder.class).setRegistryManager(registryManager);
        }
    }
}
