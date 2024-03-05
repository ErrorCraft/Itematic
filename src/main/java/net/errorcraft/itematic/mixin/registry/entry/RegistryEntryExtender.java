package net.errorcraft.itematic.mixin.registry.entry;

import net.errorcraft.itematic.access.registry.entry.RegistryEntryAccess;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

public interface RegistryEntryExtender {
    @Mixin(RegistryEntry.Reference.class)
    class ReferenceExtender<T> implements RegistryEntryAccess<T> {
        @Shadow
        @Nullable
        private
        RegistryKey<T> registryKey;

        @Unique
        private int rawId;

        @Override
        public int itematic$rawId() {
            return this.rawId;
        }

        @Override
        public void itematic$setRawId(int rawId) {
            this.rawId = rawId;
        }

        @Override
        public int compareTo(RegistryEntry<T> o) {
            if (o == null) {
                return 1;
            }
            if (this.registryKey == null) {
                return -1;
            }
            return o.getKey()
                .map(RegistryKey::getValue)
                .map(this.registryKey.getValue()::compareTo)
                .orElse(1);
        }
    }
}
