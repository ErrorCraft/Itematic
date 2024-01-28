package net.errorcraft.itematic.mixin.client.network;

import net.errorcraft.itematic.access.network.ClientConfigurationNetworkHandlerAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientRegistries;
import net.minecraft.item.Item;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ClientConfigurationPacketListener;
import net.minecraft.network.packet.s2c.config.ReadyS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientConfigurationNetworkHandler.class)
public abstract class ClientConfigurationNetworkHandlerExtender extends ClientCommonNetworkHandler implements ClientConfigurationPacketListener, ClientConfigurationNetworkHandlerAccess {
    @Final
    @Shadow
    private DynamicRegistryManager.Immutable registryManager;

    @Shadow
    @Final
    private ClientRegistries clientRegistries;

    @Unique
    private DynamicRegistryManager.Immutable createdRegistryManager;

    protected ClientConfigurationNetworkHandlerExtender(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Redirect(
        method = "onReady",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientRegistries;createRegistryManager(Lnet/minecraft/registry/DynamicRegistryManager;Z)Lnet/minecraft/registry/DynamicRegistryManager$Immutable;"
        )
    )
    private DynamicRegistryManager.Immutable useEarlierCreatedRegistryManager(ClientRegistries instance, DynamicRegistryManager precedingRegistryManager, boolean local) {
        DynamicRegistryManager.Immutable createdRegistryManager = this.createdRegistryManager;
        this.createdRegistryManager = null;
        return createdRegistryManager;
    }

    @Override
    public void itematic$onReady(ReadyS2CPacket packet) {
        NetworkThreadUtils.forceMainThread(packet, this, this.client);
        this.createdRegistryManager = this.clientRegistries.createRegistryManager(this.registryManager, this.connection.isLocal());
        Registry<Item> itemRegistry = this.createdRegistryManager.get(RegistryKeys.ITEM);
        this.client.getItemRenderer().itematic$reloadModelIds(itemRegistry);
        this.client.getBakedModelManager().itematic$setItemRegistry(itemRegistry);
        this.client.reloadResources().thenRun(() -> this.onReady(packet));
    }
}
