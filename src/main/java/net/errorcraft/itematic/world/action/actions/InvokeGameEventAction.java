package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;

public record InvokeGameEventAction(Holder<GameEvent> event, PositionTarget position, Optional<LootContext.EntityTarget> entity) implements Action<InvokeGameEventAction> {
    public static final MapCodec<InvokeGameEventAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BuiltInRegistries.GAME_EVENT.holderByNameCodec().fieldOf("event").forGetter(InvokeGameEventAction::event),
        PositionTarget.CODEC.fieldOf("position").forGetter(InvokeGameEventAction::position),
        LootContext.EntityTarget.CODEC.optionalFieldOf("entity").forGetter(InvokeGameEventAction::entity)
    ).apply(instance, InvokeGameEventAction::new));

    public static InvokeGameEventAction of(Holder<GameEvent> event, PositionTarget position, LootContext.EntityTarget entity) {
        return new InvokeGameEventAction(event, position, Optional.of(entity));
    }

    @Override
    public ActionType<InvokeGameEventAction> type() {
        return ActionTypes.INVOKE_GAME_EVENT;
    }

    @Override
    public boolean execute(ActionContext context) {
        Vec3 pos = context.get(this.position.contextParam());
        if (pos == null) {
            return false;
        }

        Entity entity = this.entity.map(LootContext.EntityTarget::contextParam)
            .map(context::get)
            .orElse(null);
        context.world().gameEvent(entity, this.event, pos);
        return true;
    }
}
