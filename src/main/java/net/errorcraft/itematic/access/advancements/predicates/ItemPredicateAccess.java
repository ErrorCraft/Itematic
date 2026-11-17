package net.errorcraft.itematic.access.advancements.predicates;

import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;

public interface ItemPredicateAccess {
    interface BuilderAccess {
        default ItemPredicate.Builder itematic$items(HolderSet<Item> items) {
            throw new AssertionError("Implemented via mixin");
        }
    }
}
