package net.errorcraft.itematic.world.entity.initializer;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import org.jspecify.annotations.Nullable;

public interface EntityInitializer<T extends Entity> {
    @Nullable
    T create(ActionContext context, EntitySpawnReason reason);
}
