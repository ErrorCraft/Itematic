package net.errorcraft.itematic.mixin.stat;

import com.mojang.logging.LogUtils;
import net.errorcraft.itematic.access.stat.StatTypeAccess;
import net.errorcraft.itematic.util.Util;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.StatType;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Mixin(StatType.class)
public class StatTypeExtender<T> implements StatTypeAccess<T> {
    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Shadow
    @Final
    private Registry<T> registry;

    @Unique
    private final Map<RegistryEntry<T>, Stat<T>> entryStats = new HashMap<>();

    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/PacketCodecs;registryValue(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/network/codec/PacketCodec;"
        )
    )
    private PacketCodec<RegistryByteBuf, RegistryEntry<T>> createPacketCodecUseRegistryEntry(RegistryKey<? extends Registry<T>> registry) {
        return PacketCodecs.registryEntry(registry);
    }

    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;"
        ),
        index = 0
    )
    private <V, O> Function<? super RegistryEntry<T>, ? extends Stat<T>> xmapToStatUseRegistryEntry(Function<? super V, ? extends O> to) {
        return this::itematic$getOrCreateStat;
    }

    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;"
        ),
        index = 1
    )
    private <V, O> Function<Stat<T>, ? extends RegistryEntry<T>> xmapFromStatUseRegistryEntry(Function<? super O, ? extends V> from) {
        return Stat::itematic$entry;
    }

    @Inject(
        method = "hasStat",
        at = @At("HEAD")
    )
    private void checkDynamicRegistry(T key, CallbackInfoReturnable<Boolean> info) {
        if (Objects.equals(this.registry.getKey(), RegistryKeys.ITEM)) {
            LOGGER.warn(Util.stackTraceMessage("Tried to check for a stat for an item from a value directly. This is no longer supported and should be modified to use a holder instead."));
        }
    }

    @Inject(
        method = "getOrCreateStat(Ljava/lang/Object;Lnet/minecraft/stat/StatFormatter;)Lnet/minecraft/stat/Stat;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkDynamicRegistry(T key, StatFormatter formatter, CallbackInfoReturnable<Stat<T>> info) {
        if (Objects.equals(this.registry.getKey(), RegistryKeys.ITEM)) {
            LOGGER.warn(Util.stackTraceMessage("Tried to create and get a stat for an item from a value directly. This is no longer supported and should be modified to use a holder instead."));
            info.setReturnValue(null);
        }
    }

    @Redirect(
        method = "getOrCreateStat(Ljava/lang/Object;Lnet/minecraft/stat/StatFormatter;)Lnet/minecraft/stat/Stat;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;",
            remap = false
        )
    )
    @SuppressWarnings("unchecked")
    private <K, V> V computeIfAbsentUseRegistryEntry(Map<K, V> instance, K k, Function<? super K, ? extends V> mappingFunction, T key, StatFormatter formatter) {
        return (V) this.itematic$getOrCreateStat(this.registry.getEntry(key), formatter);
    }

    @Override
    public boolean itematic$hasStat(RegistryEntry<T> entry) {
        return this.entryStats.containsKey(entry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Stat<T> itematic$getOrCreateStat(RegistryEntry<T> entry, StatFormatter formatter) {
        return this.entryStats.computeIfAbsent(entry, value -> {
            Stat<T> stat = StatAccessor.create((StatType<T>)(Object) this, value.value(), formatter);
            stat.itematic$setEntry(value);
            return stat;
        });
    }
}
