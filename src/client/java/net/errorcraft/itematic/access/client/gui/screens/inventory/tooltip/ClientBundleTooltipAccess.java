package net.errorcraft.itematic.access.client.gui.screens.inventory.tooltip;

import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import org.apache.commons.lang3.math.Fraction;

public interface ClientBundleTooltipAccess {
    default void itematic$setCapacity(Fraction capacity) {}
    default void itematic$setItemHolderRules(ItemHolderRules itemHolderRules) {}
}
