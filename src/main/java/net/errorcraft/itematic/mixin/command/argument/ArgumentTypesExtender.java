package net.errorcraft.itematic.mixin.command.argument;

import net.errorcraft.itematic.access.command.argument.ScoreboardCriterionArgumentTypeAccess;
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.Supplier;

@Mixin(ArgumentTypeInfos.class)
public class ArgumentTypesExtender {
    @Redirect(
        method = "bootstrap(Lnet/minecraft/core/Registry;)Lnet/minecraft/commands/synchronization/ArgumentTypeInfo;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/commands/synchronization/SingletonArgumentInfo;contextFree(Ljava/util/function/Supplier;)Lnet/minecraft/commands/synchronization/SingletonArgumentInfo;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=objective_criteria"
            )
        )
    )
    private static SingletonArgumentInfo<ObjectiveCriteriaArgument> scoreboardCriterionArgumentTypeUseRegistryAccess(Supplier<ObjectiveCriteriaArgument> typeSupplier) {
        return SingletonArgumentInfo.contextAware(registryAccess -> {
            ObjectiveCriteriaArgument argumentType = typeSupplier.get();
            ((ScoreboardCriterionArgumentTypeAccess) argumentType).itematic$setRegistryAccess(registryAccess);
            return argumentType;
        });
    }
}
