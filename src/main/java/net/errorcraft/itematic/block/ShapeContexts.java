package net.errorcraft.itematic.block;

import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class ShapeContexts {
    private ShapeContexts() {}

    public static ShapeContext ofNullable(@Nullable Entity entity) {
        if (entity == null) {
            return ShapeContext.absent();
        }

        return ShapeContext.of(entity);
    }
}
