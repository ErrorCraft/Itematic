package net.errorcraft.itematic.mixin.stat;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.Function;

@Mixin(ServerStatHandler.class)
public class ServerStatHandlerExtender extends StatHandler {
    @Shadow
    @Final
    private MinecraftServer server;

    @ModifyArg(
        method = "createStat",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;flatMap(Ljava/util/function/Function;)Ljava/util/Optional;"
        )
    )
    private <T, U> Function<Identifier, Optional<RegistryEntry.Reference<T>>> flatMapToEntryUseDynamicRegistry(Function<? super T, ? extends Optional<? extends U>> mapper, @Local(argsOnly = true) StatType<T> type) {
        return id -> this.server.getRegistryManager()
            .getOrThrow(type.getRegistry().getKey())
            .getEntry(id);
    }

    @ModifyArg(
        method = "createStat",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;"
        )
    )
    private <T, U> Function<RegistryEntry.Reference<T>, Stat<T>> mapToStatUseRegistryEntry(Function<? super T, ? extends U> mapper, @Local(argsOnly = true) StatType<T> type) {
        return type::itematic$getOrCreateStat;
    }

    @Redirect(
        method = "asString",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/ServerStatHandler;getStatId(Lnet/minecraft/stat/Stat;)Lnet/minecraft/util/Identifier;"
        )
    )
    private <T> Identifier getStatIdUseDynamicRegistry(Stat<T> stat) {
        return this.server.getRegistryManager()
            .getOrThrow(stat.getType().getRegistry().getKey())
            .getId(stat.getValue());
    }
}
