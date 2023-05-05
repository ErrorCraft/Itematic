package net.errorcraft.itematic.mixin.network;

import com.llamalad7.mixinextras.sugar.Local;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.errorcraft.itematic.access.network.ClientConnectionAccess;
import net.errorcraft.itematic.util.EventUtil;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.DecoderHandler;
import net.minecraft.network.PacketEncoder;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ClientConnection.class)
public class ClientConnectionExtender implements ClientConnectionAccess {
    private final Event<Consumer<DynamicRegistryManager>> setRegistryManager = EventUtil.consumer();

    @Mixin(targets = "net/minecraft/network/ClientConnection$1")
    public static class ChannelInitializerExtender {
        @Shadow
        @Final
        ClientConnection field_11663;

        @Inject(
            method = "initChannel",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/ClientConnection;addHandlers(Lio/netty/channel/ChannelPipeline;Lnet/minecraft/network/NetworkSide;)V",
                shift = At.Shift.AFTER
            )
        )
        private void initChannelSetRegistryManager(Channel channel, CallbackInfo info, @Local ChannelPipeline channelPipeline) {
            this.field_11663.addSetRegistryManagerConsumer(channelPipeline.get(DecoderHandler.class)::setRegistryManager);
            this.field_11663.addSetRegistryManagerConsumer(channelPipeline.get(PacketEncoder.class)::setRegistryManager);
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
