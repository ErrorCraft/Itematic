package net.errorcraft.itematic.mixin.server.commands;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
import net.minecraft.server.commands.ScoreboardCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ScoreboardCommand.class)
public class ScoreboardCommandExtender {
    @ModifyArg(
        method = "register",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/commands/Commands;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=criteria"
            )
        )
    )
    private static <T> ArgumentType<T> setContextForObjectiveCriteriaArgument(ArgumentType<T> type, @Local(name = "context", argsOnly = true) CommandBuildContext context) {
        ((ObjectiveCriteriaArgument) type).itematic$setContext(context);
        return type;
    }
}
