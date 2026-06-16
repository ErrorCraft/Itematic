package net.errorcraft.itematic.network.codec;

import com.mojang.datafixers.util.Function4;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.math.Fraction;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;

public class PacketCodecUtil {
    public static final PacketCodec<ByteBuf, Fraction> FRACTION = PacketCodec.tuple(
        PacketCodecs.VAR_INT, Fraction::getNumerator,
        PacketCodecs.VAR_INT, Fraction::getDenominator,
        Fraction::getFraction
    );

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

    public static <T> PacketCodec<ByteBuf, TagKey<T>> tag(RegistryKey<? extends Registry<T>> registry) {
        return Identifier.PACKET_CODEC.xmap(id -> TagKey.of(registry, id), TagKey::id);
    }

    public static <B extends ByteBuf, T> PacketCodec<B, Set<T>> set(PacketCodec<B, T> packetCodec) {
        return PacketCodecs.collection(HashSet::new, packetCodec);
    }

    public static <B extends ByteBuf, K, V> PacketCodec<B, Map<K, V>> dispatchedMap(IntFunction<? extends Map<K, V>> factory, PacketCodec<? super B, K> keyCodec, final Function<K, PacketCodec<? super B, ? extends V>> valueCodecFunction) {
        return new PacketCodec<>() {
            @Override
            public Map<K, V> decode(B buf) {
                int size = PacketCodecs.readCollectionSize(buf, Integer.MAX_VALUE);
                Map<K, V> map = factory.apply(Math.min(size, PacketCodecs.field_49674));
                for (int i = 0; i < size; i++) {
                    K key = keyCodec.decode(buf);
                    V value = valueCodecFunction.apply(key).decode(buf);
                    map.put(key, value);
                }

                return map;
            }

            @Override
            @SuppressWarnings("unchecked")
            public void encode(B buf, Map<K, V> value) {
                PacketCodecs.writeCollectionSize(buf, value.size(), Integer.MAX_VALUE);
                for (Map.Entry<K, V> entry : value.entrySet()) {
                    K key = entry.getKey();
                    keyCodec.encode(buf, key);
                    PacketCodec<? super B, V> entryPacketCodec = (PacketCodec<? super B, V>) valueCodecFunction.apply(key);
                    entryPacketCodec.encode(buf, entry.getValue());
                }
            }
        };
    }
}
