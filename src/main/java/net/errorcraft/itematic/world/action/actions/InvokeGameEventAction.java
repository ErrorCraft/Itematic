package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

public record InvokeGameEventAction(RegistryEntry<GameEvent> event, PositionTarget position, Optional<LootContext.EntityTarget> entity) implements Action<InvokeGameEventAction> {
    public static final MapCodec<InvokeGameEventAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Registries.GAME_EVENT.getEntryCodec().fieldOf("event").forGetter(InvokeGameEventAction::event),
        PositionTarget.CODEC.fieldOf("position").forGetter(InvokeGameEventAction::position),
        LootContext.EntityTarget.CODEC.optionalFieldOf("entity").forGetter(InvokeGameEventAction::entity)
    ).apply(instance, InvokeGameEventAction::new));

    public static InvokeGameEventAction of(RegistryEntry<GameEvent> event, PositionTarget position, LootContext.EntityTarget entity) {
        return new InvokeGameEventAction(event, position, Optional.of(entity));
    }

    @Override
    public ActionType<InvokeGameEventAction> type() {
        return ActionTypes.INVOKE_GAME_EVENT;
    }

    @Override
    public boolean execute(NewActionContext context) {
        Vec3d pos = context.get(this.position.parameter());
        if (pos == null) {
            return false;
        }

        Entity entity = this.entity.map(LootContext.EntityTarget::getParameter)
            .map(context::get)
            .orElse(null);
        context.world().emitGameEvent(entity, this.event, pos);
        return true;
    }
}
