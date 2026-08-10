package net.errorcraft.itematic.world.phys.shapes;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jspecify.annotations.Nullable;

public class CollisionContexts {
    private CollisionContexts() {}

    public static CollisionContext ofNullable(@Nullable Entity entity) {
        if (entity == null) {
            return CollisionContext.empty();
        }

        return CollisionContext.of(entity);
    }
}
