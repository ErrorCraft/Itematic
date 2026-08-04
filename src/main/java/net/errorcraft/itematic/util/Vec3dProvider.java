package net.errorcraft.itematic.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class Vec3dProvider {
    public static final Vec3dProvider ZERO = of(0.0d, 0.0d, 0.0d);
    public static final Codec<Vec3dProvider> CODEC = Range.DOUBLE_CODEC.listOf()
        .comapFlatMap(pos -> Util.fixedSize(pos, 3).map(Vec3dProvider::new), provider -> provider.pos);

    private final List<Range.DoubleRange> pos;

    private Vec3dProvider(List<Range.DoubleRange> pos) {
        this.pos = pos;
    }

    public Vec3dProvider(Range.DoubleRange x, Range.DoubleRange y, Range.DoubleRange z) {
        this(List.of(x, y, z));
    }

    public Vec3 get(RandomSource random) {
        return new Vec3(
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

    public static Vec3dProvider of(Vec3 pos) {
        return new Vec3dProvider(
            Range.DoubleRange.of(pos.x()),
            Range.DoubleRange.of(pos.y()),
            Range.DoubleRange.of(pos.z())
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
