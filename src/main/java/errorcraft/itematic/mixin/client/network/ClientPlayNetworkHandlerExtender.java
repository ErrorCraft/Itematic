package errorcraft.itematic.mixin.client.network;

import errorcraft.itematic.access.DynamicRegistryAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientDynamicRegistryType;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.registry.CombinedDynamicRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerExtender {
    @Shadow
    private CombinedDynamicRegistries<ClientDynamicRegistryType> combinedDynamicRegistries;

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
        method = "onGameJoin",
        at = @At("RETURN")
    )
    private void loadDynamicItemEntries(GameJoinS2CPacket packet, CallbackInfo info) {
        ((DynamicRegistryAccess) this.client.getItemRenderer()).loadDynamicEntries(this.combinedDynamicRegistries.getCombinedRegistryManager());
    }
}
