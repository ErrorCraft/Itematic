package errorcraft.itematic.mixin.network;

import errorcraft.itematic.access.network.ClientConnectionAccess;
import errorcraft.itematic.util.EventUtil;
import io.netty.channel.ChannelHandler;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.DecoderHandler;
import net.minecraft.network.PacketEncoder;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.Consumer;

@Mixin(ClientConnection.class)
public class ClientConnectionExtender implements ClientConnectionAccess {
    private final Event<Consumer<DynamicRegistryManager>> setRegistryManager = EventUtil.consumer();

    @Mixin(targets = "net/minecraft/network/ClientConnection$1")
    public static class ChannelInitializerExtender {
        @Shadow
        @Final
        ClientConnection field_11663;

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
            this.field_11663.addSetRegistryManagerConsumer(((DecoderHandler)channelHandler)::setRegistryManager);
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
            this.field_11663.addSetRegistryManagerConsumer(((PacketEncoder)channelHandler)::setRegistryManager);
            return channelHandler;
        }
    }

    @Override
    public void addSetRegistryManagerConsumer(Consumer<DynamicRegistryManager> consumer) {
        this.setRegistryManager.register(consumer);
    }

    @Override
    public void onSetRegistryManager(DynamicRegistryManager registryManager) {
        this.setRegistryManager.invoker().accept(registryManager);
    }
}
