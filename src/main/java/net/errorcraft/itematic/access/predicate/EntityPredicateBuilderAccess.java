package net.errorcraft.itematic.access.predicate;

import net.minecraft.predicate.entity.EntityPredicate;

public interface EntityPredicateBuilderAccess {
    default EntityPredicate.Builder itematic$usedItemAtLeast(int ticks) {
        return null;
    }
    default EntityPredicate.Builder itematic$inWaterOrRain(boolean inWaterOrRain) {
        return null;
    }
}
