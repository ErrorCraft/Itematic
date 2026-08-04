package net.errorcraft.itematic.block;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

public class ShapeContexts {
    private ShapeContexts() {}

    public static CollisionContext ofNullable(@Nullable Entity entity) {
        if (entity == null) {
            return CollisionContext.empty();
        }

        return CollisionContext.of(entity);
    }
}
