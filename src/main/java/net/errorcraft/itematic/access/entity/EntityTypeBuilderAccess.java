package net.errorcraft.itematic.access.entity;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.entity.initializer.EntityInitializerSupplier;
import net.minecraft.entity.Entity;

public interface EntityTypeBuilderAccess<T extends Entity> {
    default void itematic$initializer(EntityInitializer<T> initializer) {}
    default void itematic$initializer(EntityInitializerSupplier<T> initializer) {}
}
