package net.errorcraft.itematic.serialization;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PairCodec;
import net.minecraft.registry.Registry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MultiMapCodec<K, V> implements Codec<Multimap<K, V>> {
    private final Codec<List<Pair<K, V>>> listCodec;
    private final Comparator<K> keyComparator;

    public MultiMapCodec(Codec<K> keyCodec, Codec<V> valueCodec, Comparator<K> keyComparator) {
        this.listCodec = new PairCodec<>(keyCodec, valueCodec).listOf();
        this.keyComparator = keyComparator;
    }

    public static <K, V> MultiMapCodec<K, V> ofRegistry(Registry<K> registry, String key, Codec<V> valueCodec) {
        return new MultiMapCodec<>(registry.getCodec().fieldOf(key).codec(), valueCodec, Comparator.comparingInt(registry::getRawId));
    }

    @Override
    public <T> DataResult<Pair<Multimap<K, V>, T>> decode(DynamicOps<T> ops, T input) {
        Multimap<K, V> map = MultimapBuilder.treeKeys(this.keyComparator).arrayListValues().build();
        return this.listCodec.decode(ops, input)
            .apply2stable((u, v) -> {
                for (Pair<K, V> pair : u.getFirst()) {
                    v.put(pair.getFirst(), pair.getSecond());
                }
                return Pair.of(v, u.getSecond());
            }, DataResult.success(map));
    }

    @Override
    public <T> DataResult<T> encode(Multimap<K, V> input, DynamicOps<T> ops, T prefix) {
        List<Pair<K, V>> pairs = new ArrayList<>();
        for (K key : input.keySet()) {
            input.get(key).stream()
                .map(value -> Pair.of(key, value))
                .forEach(pairs::add);
        }
        return this.listCodec.encode(pairs, ops, prefix);
    }
}
