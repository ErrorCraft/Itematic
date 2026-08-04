package net.errorcraft.itematic.access.predicate;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.Property;

public interface StatePredicateAccess {
    interface BuilderAccess {
        default <T extends Comparable<T> & StringRepresentable> StatePropertiesPredicate.Builder itematic$range(Property<T> property, T min, T max) {
            return null;
        }
    }
}
