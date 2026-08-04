package net.errorcraft.itematic.entity.decoration.painting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.level.Level;

public class PaintingEntityUtil {
    private PaintingEntityUtil() {}

    public static Painting create(Level world, BlockPos pos, Direction facing) {
        return Painting.create(world, pos, facing).orElse(null);
    }
}
