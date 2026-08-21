package net.errorcraft.itematic.world.entity.initializer.initializers;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.Nullable;

public record SimpleEntityInitializer<T extends Entity>(EntityType<T> type) implements EntityInitializer<T> {
    @Override
    public @Nullable T create(ActionContext context, EntitySpawnReason reason) {
        return this.type.create(context.level(), reason);
    }
}
