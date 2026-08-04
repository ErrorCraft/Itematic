package net.errorcraft.itematic.access.registry.entry;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;

import java.util.List;

public interface RegistryEntryListAccess<T> {
    default List<Holder<T>> itematic$getRandom(RandomSource random, int count) {
        return List.of();
    }
}
