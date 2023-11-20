package net.errorcraft.itematic.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;

public class BlockPosUtil {
    public static final Codec<BlockPos> MAP_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("X").forGetter(BlockPos::getX),
        Codec.INT.fieldOf("Y").forGetter(BlockPos::getY),
        Codec.INT.fieldOf("Z").forGetter(BlockPos::getZ)
    ).apply(instance, BlockPos::new));

    private BlockPosUtil() {}
}
