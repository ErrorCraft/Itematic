package net.errorcraft.itematic.access.world.inventory.tooltip;

import org.apache.commons.lang3.math.Fraction;

public interface BundleTooltipAccess {
    default Fraction itematic$capacity() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setCapacity(Fraction capacity) {}
}
