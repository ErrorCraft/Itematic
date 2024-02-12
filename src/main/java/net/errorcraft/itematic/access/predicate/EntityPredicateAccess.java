package net.errorcraft.itematic.access.predicate;

import net.errorcraft.itematic.predicate.EntityPredicateExtraFields;

public interface EntityPredicateAccess {
    default EntityPredicateExtraFields itematic$extraFields() {
        return null;
    }
    default void itematic$setExtraFields(EntityPredicateExtraFields extraFields) {}
}
