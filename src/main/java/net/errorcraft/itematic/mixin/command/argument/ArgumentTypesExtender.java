package net.errorcraft.itematic.mixin.command.argument;

import net.errorcraft.itematic.access.command.argument.ScoreboardCriterionArgumentTypeAccess;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.ScoreboardCriterionArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.Supplier;

@Mixin(ArgumentTypes.class)
public class ArgumentTypesExtender {
    @Redirect(
        method = "register(Lnet/minecraft/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/command/argument/serialize/ConstantArgumentSerializer;of(Ljava/util/function/Supplier;)Lnet/minecraft/command/argument/serialize/ConstantArgumentSerializer;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=objective_criteria"
            )
        )
    )
    private static ConstantArgumentSerializer<ScoreboardCriterionArgumentType> scoreboardCriterionArgumentTypeUseRegistryAccess(Supplier<ScoreboardCriterionArgumentType> typeSupplier) {
        return ConstantArgumentSerializer.of(registryAccess -> {
            ScoreboardCriterionArgumentType argumentType = typeSupplier.get();
            ((ScoreboardCriterionArgumentTypeAccess) argumentType).itematic$setRegistryAccess(registryAccess);
            return argumentType;
        });
    }
}
