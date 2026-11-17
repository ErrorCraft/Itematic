package net.errorcraft.itematic.access.world.item.component;

import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import org.jspecify.annotations.Nullable;

public interface UseCooldownAccess {
    @Nullable
    default UseDurationProvider itematic$ticks() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setTicks(UseDurationProvider ticks) {}
}
