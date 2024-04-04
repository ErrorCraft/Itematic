package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

public record InvokeGameEventAction(RegistryEntry<GameEvent> event, ActionContextParameter position, Optional<ActionContextParameter> entity) implements Action<InvokeGameEventAction> {
    public static final MapCodec<InvokeGameEventAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Registries.GAME_EVENT.getEntryCodec().fieldOf("event").forGetter(InvokeGameEventAction::event),
        ActionContextParameter.CODEC.fieldOf("position").forGetter(InvokeGameEventAction::position),
        ActionContextParameter.CODEC.optionalFieldOf("entity").forGetter(InvokeGameEventAction::entity)
    ).apply(instance, InvokeGameEventAction::new));

    public static InvokeGameEventAction of(RegistryEntry<GameEvent> event, ActionContextParameter position, ActionContextParameter entity) {
        return new InvokeGameEventAction(event, position, Optional.of(entity));
    }

    @Override
    public ActionType<InvokeGameEventAction> type() {
        return ActionTypes.INVOKE_GAME_EVENT;
    }

    @Override
    public boolean execute(ActionContext context) {
        Entity entity = this.entity.flatMap(context::entity).orElse(null);
        Vec3d pos = context.position(this.position);
        context.world().emitGameEvent(entity, this.event, pos);
        return true;
    }
}
