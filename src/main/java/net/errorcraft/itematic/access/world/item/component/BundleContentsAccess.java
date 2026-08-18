package net.errorcraft.itematic.access.world.item.component;

import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;

public interface BundleContentsAccess {
    default Fraction itematic$occupancy(ItemHolderRules rules) {
        throw new AssertionError("Implemented via mixin");
    }

    interface MutableAccess {
        default void itematic$setFields(BundleContents bundleContents, Fraction capacity, ItemHolderRules rules) {}
    }
}
