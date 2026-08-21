package net.errorcraft.itematic.mixin.network;

import com.mojang.serialization.DynamicOps;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.RegistryOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RegistryFriendlyByteBuf.class)
public class RegistryFriendlyByteBufExtender extends FriendlyByteBufExtender {
    @Unique
    private DynamicOps<Tag> dynamicOps;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setDynamicOps(ByteBuf source, RegistryAccess registryAccess, CallbackInfo info) {
        this.dynamicOps = RegistryOps.create(NbtOps.INSTANCE, registryAccess);
    }

    @Override
    protected DynamicOps<Tag> dynamicOps() {
        return this.dynamicOps;
    }
}
