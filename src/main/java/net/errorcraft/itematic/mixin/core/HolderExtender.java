package net.errorcraft.itematic.mixin.core;

import net.errorcraft.itematic.access.core.HolderAccess;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Holder.class)
public interface HolderExtender<T> extends HolderAccess<T> {
    @Mixin(Holder.Reference.class)
    class ReferenceExtender<T> implements HolderAccess<T> {
        @Shadow
        @Nullable
        private
        ResourceKey<T> key;

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
        public int compareTo(Holder<T> o) {
            if (this.key == null) {
                return -1;
            }

            return o.unwrapKey()
                .map(ResourceKey::identifier)
                .map(this.key.identifier()::compareTo)
                .orElse(1);
        }
    }
}
