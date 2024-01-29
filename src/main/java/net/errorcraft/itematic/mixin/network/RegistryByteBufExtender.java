package net.errorcraft.itematic.mixin.network;

import com.mojang.serialization.DynamicOps;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RegistryByteBuf.class)
public class RegistryByteBufExtender extends PacketByteBufExtender {
    @Unique
    private DynamicOps<NbtElement> dynamicOps;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setDynamicOps(ByteBuf buf, DynamicRegistryManager registryManager, CallbackInfo info) {
        this.dynamicOps = RegistryOps.of(NbtOps.INSTANCE, registryManager);
    }

    @Override
    protected DynamicOps<NbtElement> dynamicOps() {
        return this.dynamicOps;
    }
}
