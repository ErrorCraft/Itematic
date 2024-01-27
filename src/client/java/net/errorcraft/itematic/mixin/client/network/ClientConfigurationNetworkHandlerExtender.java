package net.errorcraft.itematic.mixin.client.network;

import net.errorcraft.itematic.access.network.ClientConfigurationNetworkHandlerAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.item.Item;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ClientConfigurationPacketListener;
import net.minecraft.network.packet.s2c.config.ReadyS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientConfigurationNetworkHandler.class)
public abstract class ClientConfigurationNetworkHandlerExtender extends ClientCommonNetworkHandler implements ClientConfigurationPacketListener, ClientConfigurationNetworkHandlerAccess {
    @Shadow
    private DynamicRegistryManager.Immutable registryManager;

    protected ClientConfigurationNetworkHandlerExtender(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Override
    public void itematic$onReady(ReadyS2CPacket packet) {
        NetworkThreadUtils.forceMainThread(packet, this, this.client);
        Registry<Item> itemRegistry = this.registryManager.get(RegistryKeys.ITEM);
        this.client.getItemRenderer().itematic$reloadModelIds(itemRegistry);
        this.client.getBakedModelManager().itematic$setItemRegistry(itemRegistry);
        this.client.reloadResources().thenRun(() -> this.onReady(packet));
    }
}
