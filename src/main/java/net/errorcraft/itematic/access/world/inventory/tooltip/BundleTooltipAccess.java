package net.errorcraft.itematic.access.world.inventory.tooltip;

import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import org.apache.commons.lang3.math.Fraction;

public interface BundleTooltipAccess {
    default Fraction itematic$capacity() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setCapacity(Fraction capacity) {}
    default ItemHolderRules itematic$itemHolderRules() {
        throw new AssertionError("Implemented via mixin");
    }
    default void itematic$setItemHolderRules(ItemHolderRules itemHolderRules) {}
}
