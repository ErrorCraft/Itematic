package net.errorcraft.itematic.access.entity;

import net.errorcraft.itematic.entity.EntitySpawnCallback;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface EntityTypeAccess<T extends Entity> {
    default void itematic$setInitializer(EntityInitializer<T> initializer) {}
    default T itematic$create(ActionContext context, SpawnReason reason, BlockPos pos, @Nullable EntitySpawnCallback<T> callback, boolean allowItemData, boolean invertY) {
        return null;
    }
}
