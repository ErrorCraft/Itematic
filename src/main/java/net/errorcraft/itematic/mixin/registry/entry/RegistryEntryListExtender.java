package net.errorcraft.itematic.mixin.registry.entry;

import net.errorcraft.itematic.access.registry.entry.RegistryEntryListAccess;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(HolderSet.class)
public interface RegistryEntryListExtender<T> extends RegistryEntryListAccess<T> {
    @Mixin(HolderSet.ListBacked.class)
    abstract class ListBackedExtender<T> implements RegistryEntryListAccess<T> {
        @Shadow
        public abstract int size();

        @Shadow
        protected abstract List<Holder<T>> contents();

        @Override
        public List<Holder<T>> itematic$getRandom(RandomSource random, int count) {
            if (count <= 0) {
                return List.of();
            }

            List<Holder<T>> copy = new ArrayList<>(this.contents());
            if (this.size() <= count) {
                Util.shuffle(copy, random);
                return copy;
            }

            List<Holder<T>> picked = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                Holder<T> entry = Util.getRandomSafe(copy, random)
                    .orElseThrow();
                picked.add(entry);
                copy.remove(entry);
            }

            return picked;
        }
    }
}
