package net.errorcraft.itematic.mixin.network.handler;

import net.errorcraft.itematic.access.network.handler.DecoderHandlerAccess;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.handler.DecoderHandler;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DecoderHandler.class)
public class DecoderHandlerExtender implements DecoderHandlerAccess {
    private DynamicRegistryManager registryManager;

    @ModifyVariable(
        method = "decode",
        at = @At("STORE")
    )
    private PacketByteBuf decodeUseDynamicRegistryManager(PacketByteBuf packetByteBuf) {
        packetByteBuf.setRegistryManager(this.registryManager);
        return packetByteBuf;
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }
}
