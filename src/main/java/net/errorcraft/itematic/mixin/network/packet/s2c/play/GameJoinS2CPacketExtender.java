package net.errorcraft.itematic.mixin.network.packet.s2c.play;

import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.ServerDynamicRegistryType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameJoinS2CPacket.class)
public class GameJoinS2CPacketExtender {
    @Shadow
    @Final
    private DynamicRegistryManager.Immutable registryManager;

    @ModifyArg(
        method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/PacketByteBuf;decode(Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;)Ljava/lang/Object;"
        )
    )
    private static DynamicOps<NbtElement> initDecodeUseDynamicRegistryManager(DynamicOps<NbtElement> ops) {
        return RegistryOps.of(NbtOps.INSTANCE, BuiltinRegistries.createWrapperLookup());
    }

    @ModifyArg(
        method = "write",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/PacketByteBuf;encode(Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)V"
        )
    )
    private DynamicOps<NbtElement> writeEncodeUseDynamicRegistryManager(DynamicOps<NbtElement> ops) {
        DynamicRegistryManager registryManager = ServerDynamicRegistryType.createCombinedDynamicRegistries()
            .with(ServerDynamicRegistryType.WORLDGEN, this.registryManager)
            .getCombinedRegistryManager();
        return RegistryOps.of(NbtOps.INSTANCE, registryManager);
    }
}
