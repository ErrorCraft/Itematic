package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameters;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.util.Identifier;

import java.util.Optional;

public record RunFunctionAction(Identifier function, ActionContextParameters context) implements Action<RunFunctionAction> {
    public static final Codec<RunFunctionAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Identifier.CODEC.fieldOf("function").forGetter(RunFunctionAction::function),
        ActionContextParameters.CODEC.fieldOf("context").forGetter(RunFunctionAction::context)
    ).apply(instance, RunFunctionAction::new));

    @Override
    public ActionType<RunFunctionAction> type() {
        return ActionTypes.RUN_FUNCTION;
    }

    @Override
    public boolean execute(ActionContext context) {
        CommandFunctionManager functionManager = context.world().getServer().getCommandFunctionManager();
        Optional<CommandFunction<ServerCommandSource>> function = functionManager.getFunction(this.function);
        if (function.isEmpty()) {
            return false;
        }
        ServerCommandSource source = context.createCommandSource(this.context, functionManager);
        functionManager.execute(function.get(), source);
        return true;
    }
}
