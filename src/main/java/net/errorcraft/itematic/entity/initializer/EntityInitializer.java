package net.errorcraft.itematic.entity.initializer;

import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;

public interface EntityInitializer<T extends Entity> {
    T create(NewActionContext context, SpawnReason reason);
}
