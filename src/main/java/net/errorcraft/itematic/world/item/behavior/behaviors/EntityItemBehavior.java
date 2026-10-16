package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.component.EntityPlacerComponent;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.Block;

import java.util.Optional;
import java.util.Set;

public record EntityItemBehavior(EntityPlacerComponent entityPlacer) implements ItemBehavior<EntityItemBehavior> {
    public static final Codec<EntityItemBehavior> CODEC = EntityPlacerComponent.CODEC.xmap(
        EntityItemBehavior::new,
        EntityItemBehavior::entityPlacer
    );

    public static EntityItemBehavior of(EntitySpawner entity) {
        return new EntityItemBehavior(
            new EntityPlacerComponent(
                entity,
                Optional.empty(),
                false,
                EntityPlacerComponent.Pass.DEFAULT_PASSES
            )
        );
    }

    public static EntityItemBehavior of(EntitySpawner entity, HolderSet<Block> usesCollisionShape) {
        return new EntityItemBehavior(
            new EntityPlacerComponent(
                entity,
                Optional.of(usesCollisionShape),
                false,
                EntityPlacerComponent.Pass.DEFAULT_PASSES
            )
        );
    }

    public static EntityItemBehavior of(EntitySpawner entity, boolean allowSpawnerModification, EntityPlacerComponent.Pass... passes) {
        return new EntityItemBehavior(
            new EntityPlacerComponent(
                entity,
                Optional.empty(),
                allowSpawnerModification,
                Set.of(passes)
            )
        );
    }

    @Override
    public ItemBehaviorType<EntityItemBehavior> type() {
        return ItemBehaviorType.ENTITY;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.ENTITY_PLACER, this.entityPlacer);
    }
}
