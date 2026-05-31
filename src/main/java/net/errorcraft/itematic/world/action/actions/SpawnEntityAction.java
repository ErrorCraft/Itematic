package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

import java.util.List;
import java.util.Optional;

public record SpawnEntityAction(PositionTarget position, RegistryEntry<EntityType<?>> entity, Optional<RegistryEntry<SoundEvent>> spawnSound) implements Action<SpawnEntityAction> {
    public static final MapCodec<SpawnEntityAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(SpawnEntityAction::position),
        Registries.ENTITY_TYPE.getEntryCodec().fieldOf("entity").forGetter(SpawnEntityAction::entity),
        SoundEvent.ENTRY_CODEC.optionalFieldOf("spawn_sound").forGetter(SpawnEntityAction::spawnSound)
    ).apply(instance, SpawnEntityAction::new));

    public static SpawnEntityAction of(PositionTarget position, RegistryEntry<EntityType<?>> entity) {
        return new SpawnEntityAction(position, entity, Optional.empty());
    }

    @Override
    public ActionType<SpawnEntityAction> type() {
        return ActionTypes.SPAWN_ENTITY;
    }

    @Override
    public boolean execute(ActionContext context) {
        Entity entity = EntityPlacer.of(
            this.entity.value(),
            List.of(), // TODO field
            this.spawnSound,
            context,
            false,
            SpawnReason.COMMAND,
            null,
            false,
            this.position
        ).place();
        return entity != null;
    }
}
