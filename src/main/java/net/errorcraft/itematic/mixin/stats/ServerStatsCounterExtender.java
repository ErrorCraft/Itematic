package net.errorcraft.itematic.mixin.stats;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.StatsCounter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.function.Function;

@Mixin(ServerStatsCounter.class)
public class ServerStatsCounterExtender extends StatsCounter {
    @Unique
    private HolderLookup.Provider registries;

    @Redirect(
        method = "createTypedStatsCodec",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/Registry;byNameCodec()Lcom/mojang/serialization/Codec;"
        )
    )
    private static <T> Codec<Holder<T>> doNotUseStaticRegistry(Registry<T> instance) {
        return RegistryFixedCodec.create(instance.key());
    }

    @ModifyArg(
        method = "createTypedStatsCodec",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;flatComapMap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        ),
        index = 0
    )
    private static <T> Function<Holder<T>, Stat<?>> flatComapMapToUseRegistryEntry(Function<T, Stat<?>> to, @Local(name = "type", argsOnly = true) StatType<T> type) {
        return type::itematic$get;
    }

    @ModifyArg(
        method = "lambda$createTypedStatsCodec$0",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;success(Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"
        )
    )
    @SuppressWarnings("unchecked")
    private static <T> T getValueUseRegistryEntry(T result, @Local(name = "stat", argsOnly = true) Stat<T> stat) {
        return (T) stat.itematic$entry();
    }

    @Inject(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/stats/ServerStatsCounter;file:Ljava/nio/file/Path;",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void setRegistries(MinecraftServer server, Path file, CallbackInfo info) {
        this.registries = server.registryAccess();
    }

    @ModifyArg(
        method = "parse",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Dynamic;<init>(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)V"
        )
    )
    private <T> DynamicOps<T> useRegistryOps(DynamicOps<T> ops) {
        return this.registries.createSerializationContext(ops);
    }

    @ModifyArg(
        method = "toJson",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;encodeStart(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private <T> DynamicOps<T> encodeStartUseRegistryOps(DynamicOps<T> ops) {
        return this.registries.createSerializationContext(ops);
    }
}
