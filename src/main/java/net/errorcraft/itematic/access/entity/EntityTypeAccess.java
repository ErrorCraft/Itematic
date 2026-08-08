package net.errorcraft.itematic.access.entity;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.entity.EntitySpawnCallback;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializerSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import org.jetbrains.annotations.Nullable;

public interface EntityTypeAccess<T extends Entity> {
    default void itematic$setInitializer(EntityInitializer<T> initializer) {}
    default T itematic$create(ActionContext context, EntitySpawnReason reason, BlockPos pos, @Nullable EntitySpawnCallback callback, boolean allowItemData, boolean invertY) {
        return null;
    }

    interface BuilderAccess<T extends Entity> {
        default void itematic$initializer(EntityInitializer<T> initializer) {}
        default void itematic$initializer(EntityInitializerSupplier<T> initializer) {}
    }
}
