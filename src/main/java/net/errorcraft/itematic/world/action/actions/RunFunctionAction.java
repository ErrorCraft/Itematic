package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.command.ReturnValueConsumer;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.Optional;

public record RunFunctionAction(Identifier function, Optional<LootContext.EntityTarget> entity, Optional<PositionTarget> position) implements Action<RunFunctionAction> {
    public static final MapCodec<RunFunctionAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Identifier.CODEC.fieldOf("function").forGetter(RunFunctionAction::function),
        LootContext.EntityTarget.CODEC.optionalFieldOf("entity").forGetter(RunFunctionAction::entity),
        PositionTarget.CODEC.optionalFieldOf("position").forGetter(RunFunctionAction::position)
    ).apply(instance, RunFunctionAction::new));

    @Override
    public ActionType<RunFunctionAction> type() {
        return ActionTypes.RUN_FUNCTION;
    }

    @Override
    public boolean execute(ActionContext context) {
        MinecraftServer server = context.world().getServer();
        if (server == null) {
            return false;
        }

        CommandFunctionManager functionManager = server.getCommandFunctionManager();
        Optional<CommandFunction<ServerCommandSource>> function = functionManager.getFunction(this.function);
        if (function.isEmpty()) {
            return false;
        }

        MutableBoolean success = new MutableBoolean();
        ServerCommandSource source = context.commandSource(functionManager, this.entity, this.position)
            .mergeReturnValueConsumers((successful, returnValue) -> success.setValue(successful), ReturnValueConsumer::chain);
        functionManager.execute(function.get(), source);
        return success.booleanValue();
    }
}
