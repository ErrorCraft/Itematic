package net.errorcraft.itematic.loot.condition;

import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class LocationCheckPredicates {
    private LocationCheckPredicates() {}

    public static LocationCheckLootCondition setPosition(LocationCheckLootCondition predicate, PositionTarget position) {
        predicate.itematic$setPosition(position);
        return predicate;
    }

    public static LootCondition.Builder builder(PositionTarget position, LocationPredicate.Builder builder) {
        return () -> {
            LocationCheckLootCondition predicate = new LocationCheckLootCondition(Optional.of(builder.build()), BlockPos.ORIGIN);
            predicate.itematic$setPosition(position);
            return predicate;
        };
    }

    public static LootCondition.Builder builder(PositionTarget position, LocationPredicate.Builder builder, BlockPos offset) {
        return () -> {
            LocationCheckLootCondition predicate = new LocationCheckLootCondition(Optional.of(builder.build()), offset);
            predicate.itematic$setPosition(position);
            return predicate;
        };
    }
}
