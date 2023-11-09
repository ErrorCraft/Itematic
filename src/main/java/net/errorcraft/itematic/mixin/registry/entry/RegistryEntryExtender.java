package net.errorcraft.itematic.mixin.registry.entry;

import net.errorcraft.itematic.access.registry.entry.RegistryEntryAccess;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

public interface RegistryEntryExtender {
    @Mixin(RegistryEntry.Reference.class)
    class ReferenceExtender implements RegistryEntryAccess {
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
    }
}
