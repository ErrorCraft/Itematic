package net.errorcraft.itematic.access.predicate.item;

import net.errorcraft.itematic.item.component.ItemComponentType;
import net.minecraft.item.Item;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.Optional;
import java.util.Set;

public interface ItemPredicateAccess {
    default Optional<Set<ItemComponentType<?>>> itematic$behavior() {
        return Optional.empty();
    }
    default void itematic$setBehavior(Optional<Set<ItemComponentType<?>>> behavior) {}

    interface BuilderAccess {
        default ItemPredicate.Builder itematic$items(RegistryEntryList<Item> items) {
            return null;
        }
        default ItemPredicate.Builder itematic$behavior(ItemComponentType<?>... behavior) {
            return null;
        }
    }
}
