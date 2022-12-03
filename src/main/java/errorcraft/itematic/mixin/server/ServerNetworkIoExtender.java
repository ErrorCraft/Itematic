package errorcraft.itematic.mixin.server;

import io.netty.channel.ChannelHandler;
import net.minecraft.network.DecoderHandler;
import net.minecraft.network.PacketEncoder;
import net.minecraft.server.ServerNetworkIo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

public class ServerNetworkIoExtender {
    @Mixin(targets = "net/minecraft/server/ServerNetworkIo$1")
    public static class ChildHandlerExtender {
        @Shadow
        @Final
        ServerNetworkIo field_14112;

        @ModifyArg(
            method = "initChannel",
            at = @At(
                value = "INVOKE",
                target = "Lio/netty/channel/ChannelPipeline;addLast(Ljava/lang/String;Lio/netty/channel/ChannelHandler;)Lio/netty/channel/ChannelPipeline;",
                ordinal = 0,
                remap = false
            ),
            slice = @Slice(
                from = @At(
                    value = "CONSTANT",
                    args = "stringValue=decoder"
                )
            )
        )
        private ChannelHandler initChannelDecoderUseDynamicRegistry(ChannelHandler channelHandler) {
            ((DecoderHandler)channelHandler).setRegistryManager(this.field_14112.getServer().getRegistryManager());
            return channelHandler;
        }

        @ModifyArg(
            method = "initChannel",
            at = @At(
                value = "INVOKE",
                target = "Lio/netty/channel/ChannelPipeline;addLast(Ljava/lang/String;Lio/netty/channel/ChannelHandler;)Lio/netty/channel/ChannelPipeline;",
                ordinal = 0,
                remap = false
            ),
            slice = @Slice(
                from = @At(
                    value = "CONSTANT",
                    args = "stringValue=encoder"
                )
            )
        )
        private ChannelHandler initChannelEncoderUseDynamicRegistry(ChannelHandler channelHandler) {
            ((PacketEncoder)channelHandler).setRegistryManager(this.field_14112.getServer().getRegistryManager());
            return channelHandler;
        }
    }
}
