package net.errorcraft.itematic.entity.initializer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@FunctionalInterface
public interface EntityInitializerSupplier<T extends Entity> {
    EntityInitializer<T> create(EntityType<T> type);
}
