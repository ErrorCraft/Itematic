package net.errorcraft.itematic.mixin.command.argument;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.StringReader;
import net.errorcraft.itematic.access.command.argument.ScoreboardCriterionArgumentTypeAccess;
import net.errorcraft.itematic.scoreboard.ScoreboardCriterionUtil;
import net.errorcraft.itematic.stat.StatUtil;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ScoreboardCriterionArgumentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.stat.StatType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

@Mixin(ScoreboardCriterionArgumentType.class)
public class ScoreboardCriterionArgumentTypeExtender implements ScoreboardCriterionArgumentTypeAccess {
    @Unique
    private CommandRegistryAccess registryAccess;

    @Inject(
        method = "parse(Lcom/mojang/brigadier/StringReader;)Lnet/minecraft/scoreboard/ScoreboardCriterion;",
        at = @At("HEAD")
    )
    private void setLookup(StringReader reader, CallbackInfoReturnable<ScoreboardCriterion> info) {
        ScoreboardCriterionUtil.setLookup(this.registryAccess);
    }

    @Inject(
        method = "parse(Lcom/mojang/brigadier/StringReader;)Lnet/minecraft/scoreboard/ScoreboardCriterion;",
        at = @At("RETURN")
    )
    private void resetLookup(StringReader reader, CallbackInfoReturnable<ScoreboardCriterion> info) {
        ScoreboardCriterionUtil.setLookup(null);
    }

    @ModifyExpressionValue(
        method = "listSuggestions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/Registry;iterator()Ljava/util/Iterator;"
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/stat/StatType;getRegistry()Lnet/minecraft/registry/Registry;"
            )
        )
    )
    private <T> Iterator<RegistryEntry.Reference<T>> getRegistryUseDynamicRegistry(Iterator<T> original, @Local StatType<T> type) {
        return this.registryAccess.getOrThrow(type.getRegistry().getKey())
            .streamEntries()
            .iterator();
    }

    @Redirect(
        method = "listSuggestions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/command/argument/ScoreboardCriterionArgumentType;getStatName(Lnet/minecraft/stat/StatType;Ljava/lang/Object;)Ljava/lang/String;"
        )
    )
    @SuppressWarnings("unchecked")
    private <T> String getStatNameUseRegistryEntry(ScoreboardCriterionArgumentType instance, StatType<T> stat, Object value) {
        return StatUtil.statName(stat, (RegistryEntry.Reference<T>) value);
    }

    @Override
    public void itematic$setRegistryAccess(CommandRegistryAccess registryAccess) {
        this.registryAccess = registryAccess;
    }
}
