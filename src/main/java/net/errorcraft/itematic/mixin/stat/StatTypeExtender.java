package net.errorcraft.itematic.mixin.stat;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.access.stat.StatTypeAccess;
import net.errorcraft.itematic.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.StatType;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
    private final Map<Holder<T>, Stat<T>> entryStats = new HashMap<>();

    @Unique
    private MapCodec<Stat<T>> codec;

    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/ByteBufCodecs;registry(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/network/codec/StreamCodec;"
        )
    )
    private StreamCodec<RegistryFriendlyByteBuf, Holder<T>> createPacketCodecUseRegistryEntry(ResourceKey<? extends Registry<T>> registry) {
        return ByteBufCodecs.holderRegistry(registry);
    }

    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/StreamCodec;map(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/StreamCodec;"
        ),
        index = 0
    )
    private <V, O> Function<? super Holder<T>, ? extends Stat<T>> xmapToStatUseRegistryEntry(Function<? super V, ? extends O> to) {
        return this::itematic$getOrCreateStat;
    }

    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/StreamCodec;map(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/StreamCodec;"
        ),
        index = 1
    )
    private <V, O> Function<Stat<T>, ? extends Holder<T>> xmapFromStatUseRegistryEntry(Function<? super O, ? extends V> from) {
        return Stat::itematic$entry;
    }

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setCodec(Registry<T> registry, Component name, CallbackInfo info) {
        this.codec = RegistryFixedCodec.create(this.registry.key())
            .xmap(this::itematic$getOrCreateStat, Stat::itematic$entry)
            .fieldOf("entry");
    }

    @Inject(
        method = "contains",
        at = @At("HEAD")
    )
    private void checkDynamicRegistry(T key, CallbackInfoReturnable<Boolean> info) {
        if (Objects.equals(this.registry.key(), Registries.ITEM)) {
            LOGGER.warn(Util.stackTraceMessage("Tried to check for a stat for an item from a value directly. This is no longer supported and should be modified to use a holder instead."));
        }
    }

    @Inject(
        method = "get(Ljava/lang/Object;Lnet/minecraft/stats/StatFormatter;)Lnet/minecraft/stats/Stat;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkDynamicRegistry(T key, StatFormatter formatter, CallbackInfoReturnable<Stat<T>> info) {
        if (Objects.equals(this.registry.key(), Registries.ITEM)) {
            LOGGER.warn(Util.stackTraceMessage("Tried to create and get a stat for an item from a value directly. This is no longer supported and should be modified to use a holder instead."));
            info.setReturnValue(null);
        }
    }

    @Redirect(
        method = "get(Ljava/lang/Object;Lnet/minecraft/stats/StatFormatter;)Lnet/minecraft/stats/Stat;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;",
            remap = false
        )
    )
    @SuppressWarnings("unchecked")
    private <K, V> V computeIfAbsentUseRegistryEntry(Map<K, V> instance, K k, Function<? super K, ? extends V> mappingFunction, T key, StatFormatter formatter) {
        return (V) this.itematic$getOrCreateStat(this.registry.wrapAsHolder(key), formatter);
    }

    @Override
    public MapCodec<Stat<T>> itematic$codec() {
        return this.codec;
    }

    @Override
    public boolean itematic$hasStat(Holder<T> entry) {
        return this.entryStats.containsKey(entry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Stat<T> itematic$getOrCreateStat(Holder<T> entry, StatFormatter formatter) {
        return this.entryStats.computeIfAbsent(entry, value -> {
            Stat<T> stat = StatAccessor.create((StatType<T>)(Object) this, value.isBound() ? value.value() : null, formatter);
            stat.itematic$setEntry(value);
            return stat;
        });
    }
}
