package net.errorcraft.itematic.mixin.scoreboard;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.scoreboard.ScoreboardCriterionAccess;
import net.errorcraft.itematic.scoreboard.ScoreboardCriterionUtil;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.Function;

@Mixin(ScoreboardCriterion.class)
public class ScoreboardCriterionExtender implements ScoreboardCriterionAccess {
    @Shadow
    @Final
    @Mutable
    private String name;

    @Redirect(
        method = "getOrCreateStatCriterion(Lnet/minecraft/stat/StatType;Lnet/minecraft/util/Identifier;)Ljava/util/Optional;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/Registry;getOrEmpty(Lnet/minecraft/util/Identifier;)Ljava/util/Optional;"
        )
    )
    private static <T> Optional<RegistryEntry.Reference<T>> getOrEmptyUseDynamicRegistry(Registry<T> instance, Identifier id) {
        RegistryKey<? extends Registry<T>> key = instance.getKey();
        return ScoreboardCriterionUtil.lookup()
            .getWrapperOrThrow(key)
            .getOptional(RegistryKey.of(key, id));
    }

    @ModifyArg(
        method = "getOrCreateStatCriterion(Lnet/minecraft/stat/StatType;Lnet/minecraft/util/Identifier;)Ljava/util/Optional;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;map(Ljava/util/function/Function;)Ljava/util/Optional;"
        )
    )
    @SuppressWarnings("unchecked")
    private static <T, U> Function<? super RegistryEntry.Reference<T>, ? extends Stat<T>> mapToStatUseRegistryEntry(Function<? super T, ? extends U> mapper, @Local(argsOnly = true) StatType<T> statType) {
        return statType::itematic$getOrCreateStat;
    }

    @Override
    public void itematic$setName(String name) {
        this.name = name;
    }
}
