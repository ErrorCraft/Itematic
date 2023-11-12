package net.errorcraft.itematic.util;

import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;

public class PositionUtil {
    private PositionUtil() {}

    public static Vec3d vec3d(Position position) {
        return new Vec3d(position.getX(), position.getY(), position.getZ());
    }
}
