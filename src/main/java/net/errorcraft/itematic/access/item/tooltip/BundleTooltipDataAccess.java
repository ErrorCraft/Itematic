package net.errorcraft.itematic.access.item.tooltip;

import org.apache.commons.lang3.math.Fraction;

public interface BundleTooltipDataAccess {
    default Fraction itematic$capacity() {
        return null;
    }
    default void itematic$setCapacity(Fraction capacity) {}
}
