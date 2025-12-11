package net.errorcraft.itematic.access.component.type;

import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;

public interface BundleContentsComponentBuilderAccess {
    default void itematic$setCapacity(int capacity) {}
    default void itematic$setRules(ItemHolderRules rules) {}
}
