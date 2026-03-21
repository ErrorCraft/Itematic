package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;

public record SpawnEntityAction(PositionTarget position, EntityInitializer<?> entity) implements Action<SpawnEntityAction> {
    public static final MapCodec<SpawnEntityAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(SpawnEntityAction::position),
        EntityInitializer.CODEC.fieldOf("entity").forGetter(SpawnEntityAction::entity)
    ).apply(instance, SpawnEntityAction::new));

    public static SpawnEntityAction of(PositionTarget position, EntityInitializer<?> entity) {
        return new SpawnEntityAction(position, entity);
    }

    @Override
    public ActionType<SpawnEntityAction> type() {
        return ActionTypes.SPAWN_ENTITY;
    }

    @Override
    public boolean execute(NewActionContext context) {
        EntityPlacer placer = EntityPlacer.action(context, this.position, this.entity);
        if (placer == null) {
            return false;
        }

        return placer.place().succeeds();
    }
}
