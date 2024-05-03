package net.errorcraft.itematic.access.registry.entry;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;

import java.util.List;

public interface RegistryEntryListAccess<T> {
    default List<RegistryEntry<T>> itematic$getRandom(Random random, int count) {
        return List.of();
    }
}
