package net.errorcraft.itematic.access.predicate.item;

import net.errorcraft.itematic.predicate.item.ItemPredicateExtraFields;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;

public interface ItemPredicateAccess {
    default boolean itematic$test(ItemStack stack, Random random) {
        return false;
    }
    default ItemPredicateExtraFields itematic$extraFields() {
        return null;
    }
    default void itematic$setExtraFields(ItemPredicateExtraFields extraFields) {}
}
