package net.errorcraft.itematic.access.predicate;

import net.errorcraft.itematic.predicate.EntityPredicateExtraFields;
import net.minecraft.advancements.criterion.EntityPredicate;

public interface EntityPredicateAccess {
    default EntityPredicateExtraFields itematic$extraFields() {
        return null;
    }
    default void itematic$setExtraFields(EntityPredicateExtraFields extraFields) {}

    interface BuilderAccess {
        default EntityPredicate.Builder itematic$usedItemAtLeast(int ticks) {
            return null;
        }
        default EntityPredicate.Builder itematic$inWaterOrRain(boolean inWaterOrRain) {
            return null;
        }
    }
}
