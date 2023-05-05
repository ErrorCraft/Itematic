package net.errorcraft.itematic.mixin.client.network;

import net.errorcraft.itematic.access.DynamicRegistryAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientDynamicRegistryType;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerExtender {
    @Shadow
    private CombinedDynamicRegistries<ClientDynamicRegistryType> combinedDynamicRegistries;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private ClientConnection connection;

    @Shadow
    @Final
    private RecipeManager recipeManager;

    @Inject(
        method = "onGameJoin",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;combinedDynamicRegistries:Lnet/minecraft/registry/CombinedDynamicRegistries;",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void loadDynamicItemEntries(GameJoinS2CPacket packet, CallbackInfo info) {
        DynamicRegistryManager registryManager = this.combinedDynamicRegistries.getCombinedRegistryManager();
        this.connection.onSetRegistryManager(registryManager);
        this.recipeManager.setRegistryManager(registryManager);
        ((DynamicRegistryAccess) this.client.getItemRenderer()).loadDynamicEntries(registryManager);
        this.client.reloadResources();
    }
}
