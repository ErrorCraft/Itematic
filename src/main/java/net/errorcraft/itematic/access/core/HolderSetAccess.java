package net.errorcraft.itematic.access.core;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;

import java.util.List;

public interface HolderSetAccess<T> {
    default List<Holder<T>> itematic$getRandom(RandomSource random, int count) {
        return List.of();
    }
}
