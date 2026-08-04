package net.errorcraft.itematic.entity.initializer;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;

public interface EntityInitializer<T extends Entity> {
    T create(ActionContext context, EntitySpawnReason reason);
}
