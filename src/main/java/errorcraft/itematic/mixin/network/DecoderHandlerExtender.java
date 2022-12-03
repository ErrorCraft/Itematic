package errorcraft.itematic.mixin.network;

import errorcraft.itematic.access.registry.DynamicRegistryManagerAccess;
import net.minecraft.network.DecoderHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DecoderHandler.class)
public class DecoderHandlerExtender implements DynamicRegistryManagerAccess {
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
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }

    @Override
    public void setRegistryManager(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }
}
