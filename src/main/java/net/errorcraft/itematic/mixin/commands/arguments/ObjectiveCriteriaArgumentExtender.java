package net.errorcraft.itematic.mixin.commands.arguments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.JsonOps;
import net.errorcraft.itematic.access.commands.arguments.ObjectiveCriteriaArgumentAccess;
import net.errorcraft.itematic.scoreboard.ScoreboardCriterionUtil;
import net.errorcraft.itematic.stat.StatUtil;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
import net.minecraft.core.Holder;
import net.minecraft.stats.StatType;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Iterator;
import java.util.Optional;

@Mixin(ObjectiveCriteriaArgument.class)
public class ObjectiveCriteriaArgumentExtender implements ObjectiveCriteriaArgumentAccess {
    @Unique
    private CommandBuildContext context;

    @Redirect(
        method = "parse(Lcom/mojang/brigadier/StringReader;)Lnet/minecraft/world/scores/criteria/ObjectiveCriteria;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/scores/criteria/ObjectiveCriteria;byName(Ljava/lang/String;)Ljava/util/Optional;"
        )
    )
    private Optional<ObjectiveCriteria> useDynamicRegistry(String name) {
        return ScoreboardCriterionUtil.byName(name, this.context.createSerializationContext(JsonOps.INSTANCE));
    }

    @ModifyExpressionValue(
        method = "listSuggestions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/Registry;iterator()Ljava/util/Iterator;"
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/stats/StatType;getRegistry()Lnet/minecraft/core/Registry;"
            )
        )
    )
    private <T> Iterator<Holder.Reference<T>> getRegistryUseDynamicRegistry(Iterator<T> original, @Local StatType<T> type) {
        return this.context.lookupOrThrow(type.getRegistry().key())
            .listElements()
            .iterator();
    }

    @Redirect(
        method = "listSuggestions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/commands/arguments/ObjectiveCriteriaArgument;getName(Lnet/minecraft/stats/StatType;Ljava/lang/Object;)Ljava/lang/String;"
        )
    )
    @SuppressWarnings("unchecked")
    private <T> String getStatNameUseRegistryEntry(ObjectiveCriteriaArgument instance, StatType<T> stat, Object value) {
        return StatUtil.statName(stat, (Holder.Reference<T>) value);
    }

    @Override
    public void itematic$setContext(CommandBuildContext context) {
        this.context = context;
    }
}
