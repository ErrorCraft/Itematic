package net.errorcraft.itematic.access.component.type;

import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;
import net.minecraft.component.type.BundleContentsComponent;
import org.apache.commons.lang3.math.Fraction;

public interface BundleContentsComponentBuilderAccess {
    default void itematic$setExtraFields(BundleContentsComponent bundleContents, Fraction capacity, ItemHolderRules rules) {}
}
