package net.errorcraft.itematic.mixin.advancements.criterion;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class PlayerPredicateExtender {
    @Mixin(targets = "net/minecraft/advancements/criterion/PlayerPredicate$StatMatcher")
    public static class StatMatcherExtender {
        @Redirect(
            method = "lambda$new$0",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
            )
        )
        private static <T> Stat<T> getStatUseHolder(StatType<T> instance, T argument, @Local(name = "value", argsOnly = true) Holder<T> value) {
            return instance.itematic$get(value);
        }
    }
}
