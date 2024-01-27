package net.errorcraft.itematic.mixin.network.packet.s2c.config;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import net.errorcraft.itematic.mixin.registry.SerializableRegistriesAccessor;
import net.errorcraft.itematic.registry.RegistryCodecsUtil;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.config.DynamicRegistriesS2CPacket;
import net.minecraft.registry.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(DynamicRegistriesS2CPacket.class)
public class DynamicRegistriesS2CPacketExtender {
    @Shadow
    @Final
    private DynamicRegistryManager.Immutable registryManager;

    // The changes made here might be redundant when MC-268096 is fixed
    @Unique
    private static final List<? extends RegistryLoader.Entry<?>> NETWORK_REGISTRIES = RegistryLoader.DYNAMIC_REGISTRIES.stream()
        .map(entry -> SerializableRegistriesAccessor.getNetworkCodec(entry.key()).result()
            .map(codec -> new RegistryCodecsUtil.Entry(entry.key(), codec))
            .map(RegistryCodecsUtil.Entry::toLoaderEntry))
        .flatMap(Optional::stream)
        .toList();

    @WrapOperation(
        method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/PacketByteBuf;decode(Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;)Ljava/lang/Object;"
        )
    )
    @SuppressWarnings("unchecked")
    private static <T> T decodeUseCreatingDynamicRegistryManager(PacketByteBuf instance, DynamicOps<NbtElement> ops, Codec<T> codec, Operation<T> original) {
        CombinedDynamicRegistries<ServerDynamicRegistryType> combinedDynamicRegistries = ServerDynamicRegistryType.createCombinedDynamicRegistries();
        DynamicRegistryManager.Immutable baseRegistryManager = combinedDynamicRegistries.getPrecedingRegistryManagers(ServerDynamicRegistryType.WORLDGEN);
        return (T) loadFromBuf(instance, baseRegistryManager);
    }

    @ModifyArg(
        method = "write",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/PacketByteBuf;encode(Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/lang/Object;)Lnet/minecraft/network/PacketByteBuf;"
        )
    )
    private <T> DynamicOps<T> encodeUseDynamicRegistryManager(DynamicOps<T> ops) {
        DynamicRegistryManager registryManager = ServerDynamicRegistryType.createCombinedDynamicRegistries()
            .with(ServerDynamicRegistryType.WORLDGEN, this.registryManager)
            .getCombinedRegistryManager();
        return RegistryOps.of(ops, registryManager);
    }

    @Unique
    @SuppressWarnings("deprecation")
    private static DynamicRegistryManager.Immutable loadFromBuf(PacketByteBuf buf, DynamicRegistryManager baseRegistryManager) {
        Map<RegistryKey<? extends Registry<?>>, MutableRegistry<?>> dynamicRegistries = DynamicRegistriesS2CPacketExtender.NETWORK_REGISTRIES.stream()
            .collect(Collectors.toMap(RegistryLoader.Entry::key, entry -> new SimpleRegistry<>(entry.key(), Lifecycle.experimental())));
        RegistryOps.RegistryInfoGetter registryInfoGetter = RegistryCodecsUtil.createInfoGetter(baseRegistryManager, dynamicRegistries.values());
        RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, registryInfoGetter);

        RegistryCodecsUtil.setDynamicRegistries(dynamicRegistries);
        Codec<DynamicRegistryManager> codec = SerializableRegistriesAccessor.createCodec();
        DynamicRegistryManager.Immutable registryManager = buf.decode(ops, codec).toImmutable();
        RegistryCodecsUtil.setDynamicRegistries(null);
        return registryManager;
    }
}
