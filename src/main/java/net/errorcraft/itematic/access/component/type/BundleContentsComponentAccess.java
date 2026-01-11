package net.errorcraft.itematic.access.component.type;

import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;
import org.apache.commons.lang3.math.Fraction;

public interface BundleContentsComponentAccess {
    default Fraction itematic$occupancy(ItemHolderRules rules) {
        return null;
    }
}
