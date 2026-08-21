package net.errorcraft.itematic.access.advancements.criterion;

import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;

import java.util.Optional;

public interface EntityPredicateAccess {
    default Optional<MinMaxBounds.Ints> itematic$usedItemTicks() {
        return Optional.empty();
    }
    default void itematic$setUsedItemTicks(Optional<MinMaxBounds.Ints> usedItemTicks) {}
    default Optional<Boolean> itematic$inWaterOrRain() {
        return Optional.empty();
    }
    default void itematic$setInWaterOrRain(Optional<Boolean> inWaterOrRain) {}

    interface BuilderAccess {
        default EntityPredicate.Builder itematic$usedItemAtLeast(int ticks) {
            throw new AssertionError("Implemented via mixin");
        }
        default EntityPredicate.Builder itematic$inWaterOrRain(boolean inWaterOrRain) {
            throw new AssertionError("Implemented via mixin");
        }
    }
}
