package net.errorcraft.itematic.access.advancements.criterion;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.Property;

public interface StatePropertiesPredicateAccess {
    interface BuilderAccess {
        default <T extends Comparable<T> & StringRepresentable> StatePropertiesPredicate.Builder itematic$range(Property<T> property, T min, T max) {
            throw new AssertionError("Implemented via mixin");
        }
    }
}
