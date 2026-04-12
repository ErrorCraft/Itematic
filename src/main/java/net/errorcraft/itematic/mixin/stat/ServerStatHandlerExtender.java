package net.errorcraft.itematic.mixin.stat;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.StatType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(ServerStatHandler.class)
public class ServerStatHandlerExtender extends StatHandler {
    @Shadow
    @Final
    private MinecraftServer server;

    @Redirect(
        method = "createCodec",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/Registry;getCodec()Lcom/mojang/serialization/Codec;"
        )
    )
    private static <T> Codec<RegistryEntry<T>> getCodecUseRegistryEntry(Registry<T> instance) {
        return RegistryFixedCodec.of(instance.getKey());
    }

    @ModifyArg(
        method = "createCodec",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;flatComapMap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        ),
        index = 0
    )
    private static <T> Function<RegistryEntry<T>, Stat<?>> flatComapMapToUseRegistryEntry(Function<T, Stat<?>> to, @Local(argsOnly = true) StatType<T> statType) {
        return statType::itematic$getOrCreateStat;
    }

    @ModifyArg(
        method = "method_67581",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/DataResult;success(Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    @SuppressWarnings("unchecked")
    private static <T> T getValueUseRegistryEntry(T result, @Local(argsOnly = true) Stat<T> stat) {
        return (T) stat.itematic$entry();
    }

    @ModifyArg(
        method = "parse",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Dynamic;<init>(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)V",
            remap = false
        )
    )
    private <T> DynamicOps<T> useRegistryOps(DynamicOps<T> ops) {
        return this.server.getRegistryManager().getOps(ops);
    }

    @ModifyArg(
        method = "asString",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;encodeStart(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private <T> DynamicOps<T> encodeStartUseRegistryOps(DynamicOps<T> ops) {
        return this.server.getRegistryManager().getOps(ops);
    }
}
