package net.errorcraft.itematic.world.level.storage.loot.predicates;

import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.Optional;

public class LocationCheckPredicates {
    private LocationCheckPredicates() {}

    public static LocationCheck setPosition(LocationCheck predicate, PositionTarget position) {
        predicate.itematic$setPosition(position);
        return predicate;
    }

    public static LootItemCondition.Builder builder(PositionTarget position, LocationPredicate.Builder builder) {
        return () -> {
            LocationCheck predicate = new LocationCheck(Optional.of(builder.build()), BlockPos.ZERO);
            predicate.itematic$setPosition(position);
            return predicate;
        };
    }

    public static LootItemCondition.Builder builder(PositionTarget position, LocationPredicate.Builder builder, BlockPos offset) {
        return () -> {
            LocationCheck predicate = new LocationCheck(Optional.of(builder.build()), offset);
            predicate.itematic$setPosition(position);
            return predicate;
        };
    }
}
