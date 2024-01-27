package net.errorcraft.itematic.network.codec;

import com.mojang.datafixers.util.Function4;
import net.minecraft.network.codec.PacketCodec;

import java.util.function.Function;

public class PacketCodecUtil {
    private PacketCodecUtil() {}

    public static <B, C, T1, T2, T3, T4> PacketCodec<B, C> tuple(
        final PacketCodec<? super B, T1> codec1, final Function<C, T1> from1,
        final PacketCodec<? super B, T2> codec2, final Function<C, T2> from2,
        final PacketCodec<? super B, T3> codec3, final Function<C, T3> from3,
        final PacketCodec<? super B, T4> codec4, final Function<C, T4> from4,
        final Function4<T1, T2, T3, T4, C> to) {
        return new PacketCodec<>() {

            @Override
            public C decode(B buf) {
                T1 t1 = codec1.decode(buf);
                T2 t2 = codec2.decode(buf);
                T3 t3 = codec3.decode(buf);
                T4 t4 = codec4.decode(buf);
                return to.apply(t1, t2, t3, t4);
            }

            @Override
            public void encode(B buf, C value) {
                codec1.encode(buf, from1.apply(value));
                codec2.encode(buf, from2.apply(value));
                codec3.encode(buf, from3.apply(value));
                codec4.encode(buf, from4.apply(value));
            }
        };
    }
}
