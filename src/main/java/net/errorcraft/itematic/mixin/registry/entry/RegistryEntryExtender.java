package net.errorcraft.itematic.mixin.registry.entry;

import net.errorcraft.itematic.access.registry.entry.RegistryEntryAccess;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;

public interface RegistryEntryExtender {
    @Mixin(RegistryEntry.Reference.class)
    class ReferenceExtender implements RegistryEntryAccess {
        private int rawId;

        @Override
        public int getRawId() {
            return this.rawId;
        }

        @Override
        public void setRawId(int rawId) {
            this.rawId = rawId;
        }
    }
}
