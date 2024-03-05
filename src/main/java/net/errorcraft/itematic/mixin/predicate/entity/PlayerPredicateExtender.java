package net.errorcraft.itematic.mixin.predicate.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class PlayerPredicateExtender {
    @Mixin(targets = "net.minecraft.predicate.entity.PlayerPredicate$StatMatcher")
    public static class StatMatcherExtender {
        @Redirect(
            method = "method_53226",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
            )
        )
        @SuppressWarnings("unchecked")
        private static <T> Stat<T> getOrCreateStatUseRegistryEntry(StatType<T> instance, T key, @Local(argsOnly = true) RegistryEntry<T> entry) {
            return instance.itematic$getOrCreateStat(entry);
        }
    }
}
