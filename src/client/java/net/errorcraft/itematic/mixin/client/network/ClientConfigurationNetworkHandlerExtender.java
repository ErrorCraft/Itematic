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
import net.minecraft.resource.ResourceFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(ClientConfigurationNetworkHandler.class)
public abstract class ClientConfigurationNetworkHandlerExtender extends ClientCommonNetworkHandler implements ClientConfigurationPacketListener, ClientConfigurationNetworkHandlerAccess {
    @Shadow
    protected abstract <T> T openClientDataPack(Function<ResourceFactory, T> opener);

    @Shadow
    protected abstract DynamicRegistryManager.Immutable method_57043(ResourceFactory par1);

    @Unique
    private DynamicRegistryManager.Immutable createdRegistryManager;

    protected ClientConfigurationNetworkHandlerExtender(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Redirect(
        method = "onReady",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientConfigurationNetworkHandler;openClientDataPack(Ljava/util/function/Function;)Ljava/lang/Object;"
        )
    )
    @SuppressWarnings("unchecked")
    private <T> T useEarlierCreatedRegistryManager(ClientConfigurationNetworkHandler instance, Function<ResourceFactory, T> opener) {
        DynamicRegistryManager.Immutable createdRegistryManager = this.createdRegistryManager;
        this.createdRegistryManager = null;
        return (T) createdRegistryManager;
    }

    @Override
    public void itematic$onReady(ReadyS2CPacket packet) {
        NetworkThreadUtils.forceMainThread(packet, this, this.client);
        this.createdRegistryManager = this.openClientDataPack(this::method_57043);
        Registry<Item> itemRegistry = this.createdRegistryManager.get(RegistryKeys.ITEM);
        this.client.getItemRenderer().itematic$reloadModelIds(itemRegistry);
        this.client.getBakedModelManager().itematic$setItemRegistry(itemRegistry);
        this.client.reloadResources().thenRun(() -> this.onReady(packet));
    }
}
