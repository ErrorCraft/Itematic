package net.errorcraft.itematic.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import java.util.List;

public class Vec3dProvider {
    public static final Vec3dProvider ZERO = of(0.0d, 0.0d, 0.0d);
    public static final Codec<Vec3dProvider> CODEC = Range.DOUBLE_CODEC.listOf()
        .comapFlatMap(pos -> Util.decodeFixedLengthList(pos, 3).map(Vec3dProvider::new), provider -> provider.pos);

    private final List<Range.DoubleRange> pos;

    private Vec3dProvider(List<Range.DoubleRange> pos) {
        this.pos = pos;
    }

    public Vec3dProvider(Range.DoubleRange x, Range.DoubleRange y, Range.DoubleRange z) {
        this(List.of(x, y, z));
    }

    public Vec3d get(Random random) {
        return new Vec3d(
            this.pos.get(0).get(random),
            this.pos.get(1).get(random),
            this.pos.get(2).get(random)
        );
    }

    public static Vec3dProvider of(double x, double y, double z) {
        return new Vec3dProvider(
            Range.DoubleRange.of(x),
            Range.DoubleRange.of(y),
            Range.DoubleRange.of(z)
        );
    }

    public static Vec3dProvider of(Vec3d pos) {
        return new Vec3dProvider(
            Range.DoubleRange.of(pos.getX()),
            Range.DoubleRange.of(pos.getY()),
            Range.DoubleRange.of(pos.getZ())
        );
    }

    public static Vec3dProvider of(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        return new Vec3dProvider(
            Range.DoubleRange.of(minX, maxX),
            Range.DoubleRange.of(minY, maxY),
            Range.DoubleRange.of(minZ, maxZ)
        );
    }
}
