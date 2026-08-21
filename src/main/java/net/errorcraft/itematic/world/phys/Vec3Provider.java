package net.errorcraft.itematic.world.phys;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.util.RandomRange;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Vec3Provider {
    public static final Vec3Provider ZERO = exactly(0.0d, 0.0d, 0.0d);
    public static final Codec<Vec3Provider> CODEC = RandomRange.Doubles.CODEC.listOf()
        .comapFlatMap(
            entries -> Util.fixedSize(entries, 3).map(Vec3Provider::new),
            provider -> List.of(provider.x, provider.y, provider.z)
        );

    private final RandomRange.Doubles x;
    private final RandomRange.Doubles y;
    private final RandomRange.Doubles z;

    private Vec3Provider(List<RandomRange.Doubles> ranges) {
        this(ranges.get(0), ranges.get(1), ranges.get(2));
    }

    public Vec3Provider(RandomRange.Doubles x, RandomRange.Doubles y, RandomRange.Doubles z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vec3Provider exactly(double x, double y, double z) {
        return new Vec3Provider(
            RandomRange.Doubles.exactly(x),
            RandomRange.Doubles.exactly(y),
            RandomRange.Doubles.exactly(z)
        );
    }

    public static Vec3Provider of(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        return new Vec3Provider(
            RandomRange.Doubles.of(minX, maxX),
            RandomRange.Doubles.of(minY, maxY),
            RandomRange.Doubles.of(minZ, maxZ)
        );
    }

    public Vec3 get(RandomSource random) {
        return new Vec3(
            this.x.get(random),
            this.y.get(random),
            this.z.get(random)
        );
    }
}
