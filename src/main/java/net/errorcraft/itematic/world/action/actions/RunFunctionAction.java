package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.commands.CommandResultCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.world.level.storage.loot.LootContext;
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
        return ActionType.RUN_FUNCTION;
    }

    @Override
    public boolean execute(ActionContext context) {
        MinecraftServer server = context.level().getServer();
        if (server == null) {
            return false;
        }

        ServerFunctionManager functionManager = server.getFunctions();
        Optional<CommandFunction<CommandSourceStack>> function = functionManager.get(this.function);
        if (function.isEmpty()) {
            return false;
        }

        MutableBoolean success = new MutableBoolean();
        CommandSourceStack source = context.commandSource(functionManager, this.entity, this.position)
            .withCallback((successful, returnValue) -> success.setValue(successful), CommandResultCallback::chain);
        functionManager.execute(function.get(), source);
        return success.booleanValue();
    }
}
