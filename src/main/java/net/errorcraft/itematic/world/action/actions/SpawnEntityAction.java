package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.world.item.placement.EntityPlacer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;

public record SpawnEntityAction(EntitySpawner entity, PositionTarget position) implements Action<SpawnEntityAction> {
    public static final MapCodec<SpawnEntityAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        EntitySpawner.CODEC.fieldOf("entity").forGetter(SpawnEntityAction::entity),
        PositionTarget.CODEC.fieldOf("position").forGetter(SpawnEntityAction::position)
    ).apply(instance, SpawnEntityAction::new));

    public static SpawnEntityAction of(Holder<EntityType<?>> entity, PositionTarget position) {
        return new SpawnEntityAction(EntitySpawner.of(entity), position);
    }

    @Override
    public ActionType<SpawnEntityAction> type() {
        return ActionType.SPAWN_ENTITY;
    }

    @Override
    public boolean execute(ActionContext context) {
        EntityPlacer placer = EntityPlacer.of(this.entity, null);
        return placer.place(context, this.position, EntitySpawnReason.COMMAND) != null;
    }
}
