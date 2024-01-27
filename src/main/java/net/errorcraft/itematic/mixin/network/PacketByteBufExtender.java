package net.errorcraft.itematic.mixin.network;

import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PacketByteBuf.class)
public class PacketByteBufExtender {
    @Unique
    protected DynamicOps<NbtElement> dynamicOps() {
        return NbtOps.INSTANCE;
    }

    @ModifyArg(
        method = "decode(Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Lnet/minecraft/nbt/NbtSizeTracker;)Ljava/lang/Object;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private DynamicOps<NbtElement> parseUseCustomDynamicOps(DynamicOps<NbtElement> ops) {
        if (ops instanceof RegistryOps<NbtElement>) {
            return ops;
        }
        return this.dynamicOps();
    }

    @ModifyArg(
        method = "encode(Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)Lnet/minecraft/network/PacketByteBuf;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;encodeStart(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private DynamicOps<NbtElement> encodeStartUseCustomDynamicOps(DynamicOps<NbtElement> ops) {
        if (ops instanceof RegistryOps<NbtElement>) {
            return ops;
        }
        return this.dynamicOps();
    }
}
