package net.errorcraft.itematic.access.predicate.item;

import net.errorcraft.itematic.item.component.ItemComponentType;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.entry.RegistryEntryList;

public interface ItemPredicateBuilderAccess {
    default ItemPredicate.Builder itematic$items(RegistryEntryList<Item> items) {
        return null;
    }
    default ItemPredicate.Builder itematic$behavior(ItemComponentType<?>... behavior) {
        return null;
    }
    default ItemPredicate.Builder itematic$dataComponents(ComponentType<?>... dataComponents) {
        return null;
    }
}
