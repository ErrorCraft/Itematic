package net.errorcraft.itematic.access.advancements.predicates.entity;

import net.minecraft.advancements.predicates.entity.EntityPredicate;

public interface EntityPredicateAccess {
    interface BuilderAccess {
        default EntityPredicate.Builder itematic$usedItemAtLeast(int ticks) {
            throw new AssertionError("Implemented via mixin");
        }
        default EntityPredicate.Builder itematic$inWaterOrRain(boolean inWaterOrRain) {
            throw new AssertionError("Implemented via mixin");
        }
    }
}
