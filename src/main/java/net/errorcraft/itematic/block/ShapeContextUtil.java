package net.errorcraft.itematic.block;

import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;

public class ShapeContextUtil {
    private ShapeContextUtil() {}

    public static ShapeContext ofNullable(Entity entity) {
        if (entity == null) {
            return ShapeContext.absent();
        }
        return ShapeContext.of(entity);
    }
}
