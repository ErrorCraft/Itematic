package net.errorcraft.itematic.access.predicate;

import net.minecraft.predicate.StatePredicate;
import net.minecraft.state.property.Property;
import net.minecraft.util.StringIdentifiable;

public interface StatePredicateAccess {
    interface BuilderAccess {
        default <T extends Comparable<T> & StringIdentifiable> StatePredicate.Builder itematic$range(Property<T> property, T min, T max) {
            return null;
        }
    }
}
