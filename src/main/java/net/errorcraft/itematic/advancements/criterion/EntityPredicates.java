package net.errorcraft.itematic.advancements.criterion;

import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;

import java.util.Optional;

public class EntityPredicates {
    private EntityPredicates() {}

    public static EntityPredicate setFields(EntityPredicate predicate, Optional<MinMaxBounds.Ints> usedItemTicks, Optional<Boolean> inWaterOrRain) {
        predicate.itematic$setUsedItemTicks(usedItemTicks);
        predicate.itematic$setInWaterOrRain(inWaterOrRain);
        return predicate;
    }
}
