package net.errorcraft.itematic.mixin.client.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.config.DynamicRegistriesS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConfigurationNetworkHandler.class)
public abstract class ClientConfigurationNetworkHandlerExtender extends ClientCommonNetworkHandler {
    @Shadow
    private DynamicRegistryManager.Immutable registryManager;

    protected ClientConfigurationNetworkHandlerExtender(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(
        method = "onDynamicRegistries",
        at = @At("TAIL")
    )
    private void applyDynamicRegistries(DynamicRegistriesS2CPacket packet, CallbackInfo info) {
        this.connection.onSetRegistryManager(this.registryManager);
        this.client.getItemRenderer().reloadModelIds(this.registryManager.get(RegistryKeys.ITEM));
        this.client.reloadResources();
    }
}
