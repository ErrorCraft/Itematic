package net.errorcraft.itematic.entity.decoration.painting;

import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PaintingEntityUtil {
    private PaintingEntityUtil() {}

    public static PaintingEntity create(World world, BlockPos pos, Direction facing) {
        return PaintingEntity.placePainting(world, pos, facing).orElse(null);
    }
}
