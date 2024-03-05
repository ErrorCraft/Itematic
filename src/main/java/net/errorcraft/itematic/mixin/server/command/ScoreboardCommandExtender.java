package net.errorcraft.itematic.mixin.server.command;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.arguments.ArgumentType;
import net.errorcraft.itematic.access.command.argument.ScoreboardCriterionArgumentTypeAccess;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ScoreboardCommand;
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
            target = "Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=criteria"
            )
        )
    )
    private static <T> ArgumentType<T> criterionArgumentTypeSetRegistryAccess(ArgumentType<T> type, @Local(argsOnly = true) CommandRegistryAccess registryAccess) {
        ((ScoreboardCriterionArgumentTypeAccess) type).itematic$setRegistryAccess(registryAccess);
        return type;
    }
}
