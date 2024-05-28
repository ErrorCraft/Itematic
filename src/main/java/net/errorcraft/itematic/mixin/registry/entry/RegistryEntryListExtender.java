package net.errorcraft.itematic.mixin.registry.entry;

import net.errorcraft.itematic.access.registry.entry.RegistryEntryListAccess;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(RegistryEntryList.class)
public interface RegistryEntryListExtender<T> extends RegistryEntryListAccess<T> {
    @Mixin(RegistryEntryList.ListBacked.class)
    abstract class ListBackedExtender<T> implements RegistryEntryListAccess<T> {
        @Shadow
        public abstract int size();

        @Shadow
        protected abstract List<RegistryEntry<T>> getEntries();

        @Override
        public List<RegistryEntry<T>> itematic$getRandom(Random random, int count) {
            if (count <= 0) {
                return List.of();
            }

            List<RegistryEntry<T>> copy = new ArrayList<>(this.getEntries());
            if (this.size() <= count) {
                Util.shuffle(copy, random);
                return copy;
            }

            List<RegistryEntry<T>> picked = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                RegistryEntry<T> entry = Util.getRandomOrEmpty(copy, random)
                    .orElseThrow();
                picked.add(entry);
                copy.remove(entry);
            }

            return picked;
        }
    }
}
