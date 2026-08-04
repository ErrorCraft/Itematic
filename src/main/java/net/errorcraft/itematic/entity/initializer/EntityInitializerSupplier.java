package net.errorcraft.itematic.entity.initializer;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

@FunctionalInterface
public interface EntityInitializerSupplier<T extends Entity> {
    EntityInitializer<T> create(EntityType<T> type);
}
