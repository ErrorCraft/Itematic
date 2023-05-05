package net.errorcraft.itematic.access.predicate.item;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;

import java.util.Set;

public interface ItemPredicateAccess {
    default void setItemKeys(Set<RegistryKey<Item>> itemKeys) {}
}
