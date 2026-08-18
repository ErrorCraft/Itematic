package net.errorcraft.itematic.access.advancements.criterion;

import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import java.util.Optional;
import java.util.Set;

public interface ItemPredicateAccess {
    default Optional<Set<ItemBehaviorType<?>>> itematic$behavior() {
        return Optional.empty();
    }
    default void itematic$setBehavior(Optional<Set<ItemBehaviorType<?>>> behavior) {}

    interface BuilderAccess {
        default ItemPredicate.Builder itematic$items(HolderSet<Item> items) {
            throw new AssertionError("Implemented via mixin");
        }
        default ItemPredicate.Builder itematic$behavior(ItemBehaviorType<?>... behavior) {
            throw new AssertionError("Implemented via mixin");
        }
    }
}
