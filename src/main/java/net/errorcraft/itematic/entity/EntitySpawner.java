package net.errorcraft.itematic.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.entity.EntityAccessor;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;

public record EntitySpawner(RegistryEntry<EntityType<?>> entity, ComponentChanges components) {
    public static final MapCodec<EntitySpawner> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Registries.ENTITY_TYPE.getEntryCodec().fieldOf("entity").forGetter(EntitySpawner::entity),
        ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter(EntitySpawner::components)
    ).apply(instance, EntitySpawner::new));

    public static EntitySpawner of(RegistryEntry<EntityType<?>> entity) {
        return new EntitySpawner(entity, ComponentChanges.EMPTY);
    }

    public static EntitySpawner of(RegistryEntry<EntityType<?>> entity, ComponentChanges components) {
        return new EntitySpawner(entity, components);
    }

    public Entity create(ActionContext context, BlockPos pos, SpawnReason reason) {
        return this.entity.value().itematic$create(
            context,
            reason,
            pos,
            this::applyComponents,
            false,
            false
        );
    }

    private void applyComponents(Entity entity, ItemStack stack) {
        ((EntityAccessor) entity).itematic$copyComponentsFrom(
            MergedComponentMap.create(ComponentMap.EMPTY, this.components)
        );
    }
}
