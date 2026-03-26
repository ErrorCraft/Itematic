package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;

public record SimpleEntityInitializer<T extends Entity>(EntityType<T> type) implements EntityInitializer<T> {
    @Override
    public T create(ActionContext context, SpawnReason reason) {
        return this.type.create(context.world(), reason);
    }
}
